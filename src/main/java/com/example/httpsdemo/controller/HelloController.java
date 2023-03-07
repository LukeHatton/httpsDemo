package com.example.httpsdemo.controller;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * project : httpsDemo
 * <p>description:
 *
 * @author : consi
 * @since : 2023/3/7
 **/
@RestController
public class HelloController {

  private final OpenAiService openAiService;

  public HelloController(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  @RequestMapping("/hello")
  public Map<String, String> sayHello() {
    return Collections.singletonMap("key", "hello world");
  }

  @RequestMapping("/chat")
  public List<String> chat(String chat) {
    System.out.println("==> 请求内容：" + chat);
    ChatCompletionRequest request = ChatCompletionRequest.builder()
      .model("gpt-3.5-turbo")
      .messages(Collections.singletonList(new ChatMessage("user", chat)))
      .temperature(0.6)
      .maxTokens(2048)
      .build();
    System.out.println("==> 已发送请求，请耐心等待响应...");
    ChatCompletionResult result = openAiService.createChatCompletion(request);
    return Collections.singletonList(result.getChoices().get(0).getMessage().getContent());
  }
}
