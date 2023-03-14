package com.example.httpsdemo.model.dao;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>用户的数据库模型对象
 *
 * @author lizhao 2023/3/14
 */
@Data
public class UserDao {
  @MongoId
  private String id;
  private String name;
  private String password;
}
