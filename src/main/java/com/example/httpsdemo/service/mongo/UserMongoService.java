package com.example.httpsdemo.service.mongo;

import com.example.httpsdemo.model.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>用户操作服务类。对应collection: user
 *
 * @author lizhao 2023/3/14
 */
@Service
@Slf4j
public class UserMongoService {

  private final MongoTemplate mongoTemplate;

  public UserMongoService(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<UserDao> findAll(String collectionName) {
    return mongoTemplate.findAll(UserDao.class);
  }

  public void insertOne(UserDao user) {
    UserDao insert = mongoTemplate.insert(user);
    log.info("【mongo】插入的用户信息：{}", insert);
  }

  public Integer insertMany(List<UserDao> list) {
    List<UserDao> insert = mongoTemplate.insert(list);
    return insert.size();
  }
}
