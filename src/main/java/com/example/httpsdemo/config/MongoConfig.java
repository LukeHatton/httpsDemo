package com.example.httpsdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * project : httpsDemo
 * <p>description:
 * 单节点MongoDB不支持事务，如果需要使用事务，需要部署副本集（Replica Set）或分片集群（Sharded Cluster）
 *
 * @author : consi
 * @since : 2023/3/14
 **/
@Configuration
public class MongoConfig {

  @Value("${spring.data.mongodb.database}")
  private String database;


  // @Bean
  // public MongoTransactionManager mongoTransactionManager(@Qualifier("mongoDatabaseFactory") MongoDatabaseFactory dbFactory) {
  //   return new MongoTransactionManager(dbFactory);
  // }

}
