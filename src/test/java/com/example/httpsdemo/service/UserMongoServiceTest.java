package com.example.httpsdemo.service;

import com.example.httpsdemo.model.dao.UserDao;
import com.example.httpsdemo.service.mongo.UserMongoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * project : httpsDemo
 * <p>description:
 * 在单元测试中不能使用构造器注入
 *
 * @author : consi
 * @since : 2023/3/14
 **/
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
public class UserMongoServiceTest {

  @Autowired
  UserMongoService mongoService;

  @Test
  void insertOne() {
    // 手动指定id就会使用指定的id，否则会使用自动生成的id
    UserDao user = new UserDao(null, "li", "password");
    mongoService.insertOne(user);
  }

  @Test
  void findAll() {
    List<UserDao> user = mongoService.findAll();
    log.info(user.toString());
  }

}
