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

  /**
   * 测试生成SnowFlake ID，断言id的时序关系可以直接通过比较id大小得出
   *
   * @throws InterruptedException 线程睡眠中被中断
   */
  @Test
  void next() throws InterruptedException {
    for (int i = 0; i < 10; i++) {
      Long next = snowflakeGenerator.next();
      Thread.sleep(100);
      Long next1 = snowflakeGenerator.next();
      log.info("==> 测试生成雪花算法ID: {}, {}", next, next1);
      assert next1 > next;
    }
  }

}
