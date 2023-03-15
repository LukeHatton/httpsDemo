package com.example.httpsdemo.repository;

import com.example.httpsdemo.model.dao.MessageContextDao;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * project : httpsDemo
 * <p>description:
 *
 * @author : consi
 * @since : 2023/3/15
 **/
public interface MessageContextRepository extends MongoRepository<MessageContextDao, String> {
}
