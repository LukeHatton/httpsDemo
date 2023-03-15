package com.example.httpsdemo.config;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>雪花算法ID生成器
 *
 * @author lizhao 2023/3/15
 */
@Configuration
public class IdGeneratorConfig {

  @Bean
  public SnowflakeGenerator snowflakeGenerator() {
    return new SnowflakeGenerator(0, 0);
  }

}
