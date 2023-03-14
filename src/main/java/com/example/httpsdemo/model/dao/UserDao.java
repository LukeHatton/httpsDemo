package com.example.httpsdemo.model.dao;

import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>用户的数据库模型对象
 *
 * @author lizhao 2023/3/14
 */
@Document("user") // 这个注解表示当前实体类在数据库中对应的collection名称
@Data
public class UserDao {
  @MongoId
  private String id;

  private String name;

  private String password;

  @PersistenceCreator
  public UserDao(String id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }
}
