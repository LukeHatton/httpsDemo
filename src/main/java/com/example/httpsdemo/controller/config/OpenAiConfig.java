package com.example.httpsdemo.controller.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * project : httpsDemo
 * <p>description:
 *
 * @author : consi
 * @since : 2023/3/7
 **/
@Configuration
public class OpenAiConfig {

  @Bean
  public OpenAiService createOpenAiService() {
    return new OpenAiService("", Duration.ofSeconds(60));

  }
}
