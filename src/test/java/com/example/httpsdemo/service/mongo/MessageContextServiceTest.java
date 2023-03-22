package com.example.httpsdemo.service.mongo;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConverter;
import com.example.httpsdemo.model.dao.MessageContextDao;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>
 *
 * @author lizhao 2023/3/17
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
class MessageContextServiceTest {

  String JSON_ARRAY_STRING = "[\n" +
    "  {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},\n" +
    "  {\"role\": \"user\", \"content\": \"Who won the world series in 2020?\"},\n" +
    "  {\"role\": \"assistant\", \"content\": \"The Los Angeles Dodgers won the World Series in 2020.\"},\n" +
    "  {\"role\": \"user\", \"content\": \"Where was it played?\"}\n" +
    "]";

  private final MessageContextDao messageContextDao =
    new MessageContextDao("1", JSON_ARRAY_STRING, JSON_ARRAY_STRING);

  @Autowired
  private MessageContextService messageContextService;

  @Autowired
  private JSONConverter jsonConverter;

  @Test
  void findAll() {
    List<MessageContextDao> all = messageContextService.findAll();
    log.info(all.toString());
  }

  @Test
  void findWithId() {
    messageContextService.findWithId("1");
  }

  @Test
  void getSummaryListFromMessageContext() {
    messageContextService.getSummaryListFromMessageContext(messageContextDao);
  }

  @Test
  void getOriginListFromMessageContext() {
    messageContextService.getOriginListFromMessageContext(messageContextDao);
  }

  @Test
  void getChatListFromMessageContext() {
    messageContextService.getChatListFromMessageContext(messageContextDao, "origin");
    messageContextService.getChatListFromMessageContext(messageContextDao, "summary");
  }

  @Test
  void convertJSONArrayToChatMessageList() {

    JSON convert = jsonConverter.convert(JSON_ARRAY_STRING, null);
    assert convert instanceof JSONArray;
    List<ChatMessage> chatMessages = messageContextService.convertJSONArrayToChatMessageList((JSONArray) convert);
    assert chatMessages.size() > 0;
  }

}