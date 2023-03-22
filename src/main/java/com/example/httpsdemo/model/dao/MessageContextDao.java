package com.example.httpsdemo.model.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * project : httpsDemo
 * <p>description:
 * 消息上下文DAO
 * <p>
 * origin和summary字段都是json array数据类型，其示例内容如下：
 * <pre>
 *   {@code
 *     [
 *         {"role": "system", "content": "You are a helpful assistant."},
 *         {"role": "user", "content": "Who won the world series in 2020?"},
 *         {"role": "assistant", "content": "The Los Angeles Dodgers won the World Series in 2020."},
 *         {"role": "user", "content": "Where was it played?"}
 *     ]
 *   }
 * </pre>
 *
 * @author : consi
 * @since : 2023/3/15
 **/
@Document("messageContext")
@Data
@Builder
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
