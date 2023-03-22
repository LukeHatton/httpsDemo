package com.example.httpsdemo.service.mongo;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConverter;
import cn.hutool.json.JSONObject;
import com.example.httpsdemo.config.enums.ChatCompletionRoles;
import com.example.httpsdemo.model.dao.MessageContextDao;
import com.example.httpsdemo.prompt.ChatCompletionTemplate;
import com.example.httpsdemo.repository.MessageContextRepository;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project : httpsDemo
 * <p>description:
 * 消息上下文操作服务类。对应collection: messageContext
 *
 * @author : consi
 * @since : 2023/3/15
 **/
@Service
@Slf4j
public class MessageContextService {

  private final MessageContextRepository messageContextRepository;

  private final JSONConverter jsonConverter;

  private final SnowflakeGenerator snowflakeGenerator;

  public MessageContextService(MessageContextRepository messageContextRepository, JSONConverter jsonConverter,
                               SnowflakeGenerator snowflakeGenerator) {
    this.messageContextRepository = messageContextRepository;
    this.jsonConverter = jsonConverter;
    this.snowflakeGenerator = snowflakeGenerator;
  }

  public List<MessageContextDao> findAll() {
    return messageContextRepository.findAll();
  }

  public MessageContextDao findWithId(String id) {
    return messageContextRepository.findById(id).orElse(null);
  }

  /**
   * 从MessageContextDao对象中获取摘要数据，并转换为ChatMessage列表
   *
   * @param messageContextDao 封装上下文数据
   * @return ChatMessage列表 如果参数为null则返回empty List
   */
  public List<ChatMessage> getSummaryListFromMessageContext(MessageContextDao messageContextDao) {
    return getChatListFromMessageContext(messageContextDao, "summary");
  }


  /**
   * 从MessageContextDao对象中获取原文数据，并转换为ChatMessage列表
   *
   * @param messageContextDao 封装上下文数据
   * @return ChatMessage列表 如果参数为null则返回empty List
   */
  public List<ChatMessage> getOriginListFromMessageContext(MessageContextDao messageContextDao) {
    return getChatListFromMessageContext(messageContextDao, "origin");
  }

  /**
   * 从{@link MessageContextDao}对象中构建{@link ChatMessage}列表
   *
   * @param messageContextDao 包含请求信息的对象
   * @param key               要转换的数据字段 key=origin表示转换原文，key=summary表示转换摘要
   * @return {@link ChatMessage}列表
   */
  public List<ChatMessage> getChatListFromMessageContext(MessageContextDao messageContextDao, String key) {
    if (messageContextDao == null)
      return Collections.emptyList();

    String message = "origin".equals(key)
      ? messageContextDao.getOrigin()
      : messageContextDao.getSummary();
    JSON messageJson = jsonConverter.convert(message, null);
    if (messageJson instanceof JSONArray)               // 当上下文中包含数据时，其内容会是一个JSONArray
      return convertJSONArrayToChatMessageList((JSONArray) messageJson);
    else
      return Collections.emptyList();
  }

  /**
   * 将符合指定格式的{@link JSONArray}对象转换为{@link ChatMessage}列表
   *
   * @param jsonArray 包含消息上下文数据的JSONArray对象
   * @return {@link ChatMessage}列表
   */
  public List<ChatMessage> convertJSONArrayToChatMessageList(JSONArray jsonArray) {
    List<JSONObject> list = jsonArray.toList(JSONObject.class);
    return list.stream()
      .map(e -> new ChatMessage(e.get("role", String.class), e.get("content", String.class)))
      .collect(Collectors.toList());
  }

  /**
   * 构造包含上下文信息的请求数据
   *
   * @param content     用户发送的请求内容
   * @param messageList 当前会话的上下文列表
   * @return 本次调用openai API的请求数据列表
   */
  public List<ChatMessage> constructRequestMessageList(String content, List<ChatMessage> messageList) {
    List<ChatMessage> list = new ArrayList<>(messageList);
    String prompt = list.size() == 0                                // 根据上下文，取得问题模板
      ? ChatCompletionTemplate.FIRST_QUESTION
      : ChatCompletionTemplate.QUESTION_IN_CONTEXT;

    String question = prompt.replace("问题写在这里", content);  // 嵌入问题模板，生成最终问题
    list.add(new ChatMessage(ChatCompletionRoles.USER.getRole(), question));
    return list;
  }


  /**
   * 将对象保存到数据库。如果数据库中已有相同objectId的对象，则更新
   *
   * @param messageContextDao 数据对象
   */
  public void saveContext(MessageContextDao messageContextDao) {
    messageContextRepository.save(messageContextDao);
  }

  /**
   * 解析{@link ChatMessage}对象，并追加到{@link MessageContextDao}对象中
   * <p>
   * 如果传入的messageContextDao是null，说明这是会话列表中的第一个对话，需要生成新的MessageContextDao对象
   * TODO 目前这个方法的实现过于复杂了，不需要在Java代码中手动进行json转化，spring data mongodb会自动完成这项工作。明天改进
   *
   * @param chatMessage       对响应结果的简易封装
   * @param messageContextDao 当前的上下文对象
   * @return 追加写入后的messageContextDao对象
   */
  private MessageContextDao convertChatMessageToContextDao(ChatMessage chatMessage,
                                                           MessageContextDao messageContextDao) {
    String role = chatMessage.getRole();
    String content = chatMessage.getContent();
    String originFromResponse = getOriginFromResponse(content);                         // 相应内容原文
    String summaryFromResponse = getSummaryFromResponse(content);                       // 响应内容摘要
    ChatMessage originChatMessage = new ChatMessage(role, originFromResponse);          // 记录到DB的原文（origin）字段
    ChatMessage summaryChatMessage = new ChatMessage(role, summaryFromResponse);        // 记录到DB的摘要（summary）字段

    JSON convertOrigin = jsonConverter.convert(originChatMessage, null);    // mongodb仅支持json格式
    JSON convertSummary = jsonConverter.convert(summaryChatMessage, null);
    if (messageContextDao == null)
      return MessageContextDao.builder()
        .id(snowflakeGenerator.next().toString())
        .origin(convertOrigin.toString())
        .summary(convertSummary.toString())
        .build();


    String origin = messageContextDao.getOrigin();
    String summary = messageContextDao.getSummary();
    // 解析响应内容，并从中找出响应原文和响应缩略。这可以通过prompt中的关键字判断
    String jsonStringToBeAppendedToOrigin = convertOrigin.toString();
    String jsonStringToBeAppendedToSummary = convertSummary.toString();
    origin = origin.replace("]", "," + jsonStringToBeAppendedToOrigin + "]");
    summary = summary.replace("]", "," + jsonStringToBeAppendedToSummary + "]");

    messageContextDao.setOrigin(origin);
    messageContextDao.setSummary(summary);

    return messageContextDao;
  }

  // 从响应内容中取得对用户问题的回答原文
  public String getOriginFromResponse(String response) {
    int i = response.indexOf("|summary|:");
    return response.substring(0, i);
  }

  // 从响应内容中取得对用户问题的回答摘要
  public String getSummaryFromResponse(String response) {
    int i = response.indexOf("|summary|:");
    return response.substring(i, response.length() - 1);
  }


}
