package com.example.httpsdemo.service;

import com.example.httpsdemo.model.dao.UserDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>
 *
 * @author lizhao 2023/3/14
 */
@Service
public class MongoService {

  private final MongoTemplate mongoTemplate;

  public MongoService(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void find(String collectionName) {
    Criteria criteria = Criteria.where("age").gte(20);
    Query query = new Query(criteria);
    mongoTemplate.find(query, UserDao.class);
  }
}
