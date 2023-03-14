package com.example.httpsdemo.service.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * project : httpsDemo
 * <p>description:
 * collection操作服务类
 *
 * @author : consi
 * @since : 2023/3/14
 **/
@Service
public class CollectionService {

  private final MongoTemplate mongoTemplate;

  public CollectionService(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Set<String> getCollectionNames() {
    return mongoTemplate.getCollectionNames();
  }

  public boolean collectionExists(String collectionName) {
    return mongoTemplate.collectionExists(collectionName);
  }

  public void dropCollection(String collectionName) {
    mongoTemplate.dropCollection(collectionName);
  }

  /**
   * 创建集合
   *
   * @param collectionName 要创建的集合名称
   * @return 是否创建成功 true:成功 false:失败
   */
  public boolean createCollection(String collectionName) {
    mongoTemplate.createCollection(collectionName);
    return mongoTemplate.collectionExists(collectionName);
  }
}
