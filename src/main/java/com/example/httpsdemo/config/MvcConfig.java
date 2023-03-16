package com.example.httpsdemo.config;

import cn.hutool.json.JSONConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>MVC页面跳转配置
 *
 * @author lizhao 2023/3/10
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("home");
    registry.addViewController("/home").setViewName("home");
    registry.addViewController("/hello").setViewName("hello");
    registry.addViewController("/login").setViewName("login");
  }

  @Bean
  public JSONConverter jsonConverter() {
    return new JSONConverter();
  }
}
