package com.example.httpsdemo.service.mongo;

import com.example.httpsdemo.model.dao.MessageContextDao;
import com.example.httpsdemo.repository.MessageContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * project : httpsDemo
 * <p>description:
 * 消息上下文操作服务类。对应collection: messageContext
 *
 * @author : consi
 * @since : 2023/3/15
 **/
@Service
@Slf4j
public class MessageContextMongoService {

  private final MessageContextRepository messageContextRepository;

  public MessageContextMongoService(MessageContextRepository messageContextRepository) {
    this.messageContextRepository = messageContextRepository;
  }

  public List<MessageContextDao> findAll() {
    return messageContextRepository.findAll();
  }

  public MessageContextDao findWithId(String id) {
    return messageContextRepository.findById(id).orElse(null);
  }
}
