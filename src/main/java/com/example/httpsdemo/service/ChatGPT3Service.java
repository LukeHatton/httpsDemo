package com.example.httpsdemo.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;

import javax.servlet.http.HttpSession;
import java.util.concurrent.Future;

/**
 * project : httpsDemo
 * <p>description:
 * ChatGPT 3.5 API服务接口
 * <p>之所以按照API版本命名，是因为ChatGPT的开发者接口一直在更新，且Java客户端由个人开发者维护，随时可能发生大的版本更改，
 * 因此需要准备好在进行大的版本更新时换用不同的接口实现类
 *
 * @author : consi
 * @since : 2023/3/12
 **/
public interface ChatGPT3Service {

  /**
   * 从当前用户session中取得{@link OpenAiService}对象
   *
   * @param session 当前用户session
   * @param apikey  当前用户的ChatGPT api key，作为key，其对应的value为OpenAiService对象
   * @return {@link OpenAiService}对象
   */
  OpenAiService getServiceFromSession(HttpSession session, String apikey);

  /**
   * 调用模型，并获取响应结果。由于调用模型可能耗时较长，本方法需要支持异步调用
   *
   * @param service {@link OpenAiService}对象
   * @param request {@link ChatCompletionRequest}对象
   * @return {@link ChatCompletionResult}的Future对象
   */
  Future<ChatCompletionResult> getChatResult(OpenAiService service, ChatCompletionRequest request);
}
