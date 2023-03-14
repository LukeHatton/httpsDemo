package com.example.httpsdemo;

import com.example.httpsdemo.service.mongo.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

/**
 * project : httpsDemo
 * <p>description:
 *
 * @author : consi
 * @since : 2023/3/14
 **/
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
public class CollectionServiceTest {

  @Autowired
  CollectionService collectionService;

  @Test
  void createCollection() {
    boolean createSuccess = collectionService.createCollection("testCol");
    assert createSuccess;
  }

  @Test
  void getCollectionNames() {
    Set<String> collectionNames = collectionService.getCollectionNames();
    log.info(collectionNames.toString());
  }

  @Test
  void collectionExists() {
    boolean exists = collectionService.collectionExists("test");
    assert exists;
  }

  @Test
  void dropCollection() {
    collectionService.dropCollection("testCol");
  }

}
