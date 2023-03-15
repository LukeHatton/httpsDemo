package com.example.httpsdemo;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>
 *
 * @author lizhao 2023/3/15
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
public class IdGeneratorTest {

  @Autowired
  private SnowflakeGenerator snowflakeGenerator;

  @Test
  void next() {
    Long next = snowflakeGenerator.next();
    log.info("==> 测试生成雪花算法ID: {}", next);
    assert next != 0;
  }

}
