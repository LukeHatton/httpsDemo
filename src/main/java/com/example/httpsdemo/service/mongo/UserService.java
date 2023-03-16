package com.example.httpsdemo.service.mongo;

import com.example.httpsdemo.model.dao.UserDao;
import com.example.httpsdemo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>用户操作服务类。对应collection: user
 *
 * @author lizhao 2023/3/14
 */
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<UserDao> findAll() {
    return userRepository.findAll();
  }

  public void insertOne(UserDao user) {
    UserDao insert = userRepository.insert(user);
    log.info("【mongo】插入的用户信息：{}", insert);
  }

  public Integer insertMany(List<UserDao> list) {
    List<UserDao> insert = userRepository.insert(list);
    return insert.size();
  }
}
