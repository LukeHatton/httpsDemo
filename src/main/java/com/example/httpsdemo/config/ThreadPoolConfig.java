package com.example.httpsdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * project : httpsDemo
 * <p>description:
 * 线程池配置
 *
 * @author : consi
 * @since : 2023/3/13
 **/
@Configuration
@EnableAsync
public class ThreadPoolConfig {

  @Bean(name = "executorService")
  public ExecutorService getExecutor() {
    return new ThreadPoolExecutor(
      8,
      64,
      60,
      TimeUnit.SECONDS,
      new ArrayBlockingQueue<>(256),
      Executors.defaultThreadFactory(),
      new ThreadPoolExecutor.DiscardOldestPolicy());
  }
}
