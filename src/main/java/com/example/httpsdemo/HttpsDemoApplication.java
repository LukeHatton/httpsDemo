package com.example.httpsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class HttpsDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(HttpsDemoApplication.class, args);
  }

}
