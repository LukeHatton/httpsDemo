package com.example.httpsdemo.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.concurrent.Future;

/**
 * project : httpsDemo
 * <p>description:
 *
 * @author : consi
 * @since : 2023/3/13
 **/
@Slf4j
@Service
public class ChatGPT3ServiceImpl implements ChatGPT3Service {
  /**
   * 从HttpSession取得{@link OpenAiService}对象
   * 之所以这个对象要存储在session中，是为了降低每次处理用户请求时的开销
   *
   * @param session 当前用户的session
   * @param apiKey  用户提交的api-key
   * @return 使用用户api-key创建的OpenAiService对象
   */
  @Override
  public OpenAiService getServiceFromSession(HttpSession session, String apiKey) {
    // OpenAiService这个对象的生命周期应与用户session相同
    OpenAiService service;
    if (session.getAttribute("openAiService") == null) {
      service = new OpenAiService(apiKey, Duration.ofSeconds(60));
      session.setAttribute("openAiService", service);
      log.info("==> 新建OpenAiService对象");
    }
    else {
      service = (OpenAiService) session.getAttribute("openAiService");
      log.info("==> 从用户session中取得OpenAiService对象");
    }
    return service;
  }

  /**
   * 异步地调用ChatGPT ChatCompletion模型
   *
   * @param service {@link OpenAiService}对象
   * @param request {@link ChatCompletionRequest}对象
   * @return 一个Future封装地返回结果
   */
  @Override
  @Async("executorService")
  public Future<ChatCompletionResult> getChatResult(OpenAiService service, ChatCompletionRequest request) {
    StopWatch watch = new StopWatch();
    watch.start();
    ChatCompletionResult result = service.createChatCompletion(request);
    AsyncResult<ChatCompletionResult> asyncResult = new AsyncResult<>(result);
    watch.stop();
    log.info("==> 已收到ChatGPT响应，耗时：{}s", watch.getTotalTimeSeconds());
    return asyncResult;
  }
}
