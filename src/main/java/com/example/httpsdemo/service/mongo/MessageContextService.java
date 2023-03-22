package com.example.httpsdemo.service.mongo;

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

  public MessageContextService(MessageContextRepository messageContextRepository, JSONConverter jsonConverter) {
    this.messageContextRepository = messageContextRepository;
    this.jsonConverter = jsonConverter;
  }

  public List<MessageContextDao> findAll() {
    return messageContextRepository.findAll();
  }

  public MessageContextDao findWithId(String id) {
    return messageContextRepository.findById(id).orElse(null);
  }

  // 从MessageContextDao对象中获取摘要数据，并转换为ChatMessage列表
  public List<ChatMessage> getSummaryListFromMessageContext(MessageContextDao messageContextDao) {
    return getChatListFromMessageContext(messageContextDao, "summary");
  }


  // 从MessageContextDao对象中获取原文数据，并转换为ChatMessage列表
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
      return null;

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

    String question = prompt.replace("问题写在这里", content);  // 生成最终问题
    list.add(new ChatMessage(ChatCompletionRoles.USER.getRole(), question));
    return list;
  }

}
