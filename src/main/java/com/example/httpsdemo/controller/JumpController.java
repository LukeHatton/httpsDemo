package com.example.httpsdemo.controller;

import com.example.httpsdemo.model.dao.MessageContextDao;
import com.example.httpsdemo.model.dto.OpenAiDto;
import com.example.httpsdemo.service.ChatGPT3Service;
import com.example.httpsdemo.service.mongo.MessageContextService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>演示通过thymeleaf进行基本的web响应与表单提交
 * <p>参考链接：<a href="https://www.baeldung.com/spring-mvc-thymeleaf-data">Spring MVC Data and Thymeleaf</a>
 *
 * @author lizhao 2023/3/9
 */
@Controller
@Slf4j
public class JumpController {

  private final ChatGPT3Service chatGPT3Service;

  private final MessageContextService messageContextService;

  public JumpController(ChatGPT3Service chatGPT3Service,
                        MessageContextService messageContextService) {
    this.chatGPT3Service = chatGPT3Service;
    this.messageContextService = messageContextService;
  }

  @GetMapping("/index")
  public String toIndex(ModelMap modelMap) {
    modelMap.addAttribute("formData", new OpenAiDto());
    return "index";
  }

  /**
   * 调用ChatCompletion模型，进行简单的聊天
   * <p>
   * 调用这个模型，无法利用代码生成功能，可能需要单独调用Codex模型
   *
   * @param openAiDto 表单数据，包含api key和请求内容
   * @param modelMap  modelMap
   * @param session   HttpSession
   * @return index.html
   */
  @PostMapping("/index")
  public String submitIndex(@ModelAttribute OpenAiDto openAiDto,
                            ModelMap modelMap,
                            HttpSession session) {

    /* ================ 构建请求 ================= */
    String objectId = openAiDto.getObjectId();
    String apiKey = openAiDto.getApiKey();
    String content = openAiDto.getContent();
    log.info("==> 接收到表单提交数据：objectId={}, api-key={}, content={}", objectId, apiKey, content);
    // 将缩略上下文封装到模型上下文List中
    MessageContextDao messageContextDao = messageContextService.findWithId(objectId);
    List<ChatMessage> chatMessageList = messageContextService.getSummaryListFromMessageContext(messageContextDao);
    chatMessageList.add(new ChatMessage("user", content));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
      .model("gpt-3.5-turbo")
      .messages(chatMessageList)
      .temperature(0.6)
      .maxTokens(2048)
      .build();

    /* ================ 发送请求 ================= */
    // OpenAiService这个对象的生命周期应与用户session相同
    OpenAiService openAiService = chatGPT3Service.getServiceFromSession(session, apiKey);
    log.info("==> 已发送请求，请耐心等待响应...");
    Future<ChatCompletionResult> result = chatGPT3Service.getChatResult(openAiService, request);

    /* ================ 回写数据 ================= */
    /*
     * 当得到响应后，需要将用户的问题与AI回答原文、缩略更新到MongoDB中
     * 这个操作可以异步完成
     */



    /* ============= Model & View ============== */
    modelMap.addAttribute("formData", openAiDto);
    if (StringUtils.isEmpty(apiKey))
      modelMap.addAttribute("result", "您提供的api-key不正确，请确认！");
    else {
      try {
        modelMap.addAttribute("result", result.get().getChoices().get(0).getMessage().getContent());
      } catch (InterruptedException | ExecutionException e) {
        log.error("==> 异步调用ChatGPT API异常", e);
        modelMap.addAttribute("result", "后端服务异常！请联系管理员或稍后重试");
      }
    }
    return "index";
  }

}
