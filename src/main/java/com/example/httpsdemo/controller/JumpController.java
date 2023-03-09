package com.example.httpsdemo.controller;

import com.example.httpsdemo.model.FormData;
import com.example.httpsdemo.model.OpenAiModel;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.util.StringUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

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

  @GetMapping("/showViewPage")
  public String showPage(Model model) {
    Map<String, String> map = Collections.singletonMap("spring", "mvc");
    model.addAttribute("message", "hello world");
    model.mergeAttributes(map);   // 这种方式设置的值在模版页面中静态分析无法正确解析，会在IDE中报错，但实际没有影响
    return "view/viewPage";
  }

  @GetMapping("/form")
  public String form(ModelMap modelMap) {
    // 在使用thymeleaf作为模板引擎时，必须先填充表单用到的字段，否则会导致模板解析失败
    modelMap.addAttribute("formData", new FormData());
    modelMap.addAttribute("time", new Date());
    return "view/form";
  }

  /**
   * 被{@link ModelAttribute}标注的属性会被自动添加到view中
   * <p>
   * idea对thymeleaf的智能分析支持只能识别显示添加到modelMap中的属性，对于用ModelAttribute注解标注的属性，
   * idea并不能在模板中正常识别
   *
   * @param formData 表单数据
   * @param modelMap view数据
   * @return 跳转页面
   */
  @PostMapping("/form/{time}")
  public String submitForm(@ModelAttribute FormData formData,
                           @PathVariable String time,
                           ModelMap modelMap) {
    log.info("==> 接收到表单提交数据：user={}, email={}, time={}", formData.getName(), formData.getEmail(), time);
    modelMap.addAttribute("message", "hello world");
    modelMap.addAttribute("formData", formData);
    modelMap.addAttribute("text",
      "海客谈瀛洲，烟涛微茫信难求；\n" +
        "        越人语天姥，云霞明灭或可睹。\n" +
        "        天姥连天向天横，势拔五岳掩赤城。\n" +
        "        天台四万八千丈，对此欲倒东南倾。\n" +
        "        我欲因之梦吴越，一夜飞度镜湖月。\n" +
        "        湖月照我影，送我至剡溪。\n" +
        "        谢公宿处今尚在，渌水荡漾清猿啼。\n" +
        "        脚著谢公屐，身登青云梯。\n" +
        "        半壁见海日，空中闻天鸡。\n" +
        "        千岩万转路不定，迷花倚石忽已暝。\n" +
        "        熊咆龙吟殷岩泉，栗深林兮惊层巅。\n" +
        "        云青青兮欲雨，水澹澹兮生烟。\n" +
        "        列缺霹雳，丘峦崩摧。\n" +
        "        洞天石扉，訇然中开。\n" +
        "        青冥浩荡不见底，日月照耀金银台。\n" +
        "        霓为衣兮风为马，云之君兮纷纷而来下。\n" +
        "        虎鼓瑟兮鸾回车，仙之人兮列如麻。\n" +
        "        忽魂悸以魄动，恍惊起而长嗟。\n" +
        "        惟觉时之枕席，失向来之烟霞。\n" +
        "        世间行乐亦如此，古来万事东流水。\n" +
        "        别君去兮何时还？且放白鹿青崖间，须行即骑访名山。\n" +
        "        安能摧眉折腰事权贵，使我不得开心颜！");
    return "view/viewPage";
  }

  @GetMapping("/index")
  public String toIndex(ModelMap modelMap) {
    modelMap.addAttribute("formData", new OpenAiModel());
    return "index";
  }

  @PostMapping("/index")
  public String submitIndex(@ModelAttribute OpenAiModel formData, ModelMap modelMap) {
    log.info("==> 接收到表单提交数据：api-key={}, content={}", formData.getApiKey(), formData.getContent());
    ChatCompletionRequest request = ChatCompletionRequest.builder()
      .model("gpt-3.5-turbo")
      .messages(Collections.singletonList(new ChatMessage("user", formData.getContent())))
      .temperature(0.6)
      .maxTokens(2048)
      .build();
    log.info("==> 已发送请求，请耐心等待响应...");
    StopWatch watch = new StopWatch();
    watch.start();
    // OpenAiService这个对象的生命周期应与用户session相同，下面的这种实现相当于每个请求都重新创建一个实例，会对性能造成影响
    OpenAiService openAiService = new OpenAiService("", Duration.ofSeconds(60));
    ChatCompletionResult result = openAiService.createChatCompletion(request);
    watch.stop();
    log.info("==> 已收到响应，耗时：{}s", watch.getTotalTimeSeconds());

    modelMap.addAttribute("formData", formData);
    if (StringUtils.isEmpty(formData.getApiKey()))
      modelMap.addAttribute("result", "您提供的api-key不正确，请确认！");
    else
      modelMap.addAttribute("result", result.getChoices().get(0).getMessage().getContent());

    return "index";
  }

}
