package com.example.httpsdemo.model.dao;

import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * project : httpsDemo
 * <p>description:
 *
 * @author : consi
 * @since : 2023/3/15
 **/
@Document("messageContext")
@Data
public class MessageContextDao {

  @MongoId
  private String id;

  private String origin;

  private String summary;

  @PersistenceCreator
  public MessageContextDao(String id, String origin, String summary) {
    this.id = id;
    this.origin = origin;
    this.summary = summary;
  }
}
