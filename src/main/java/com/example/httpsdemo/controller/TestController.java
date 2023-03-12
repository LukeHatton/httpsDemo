package com.example.httpsdemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>
 *
 * @author lizhao 2023/3/10
 */
@RestController
public class TestController {

  @RequestMapping("/userInfo")
  public Map<String, Object> getUserInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Map<String, Object> map = new HashMap<>();
    map.put("principal", authentication.getPrincipal());
    map.put("name", authentication.getName());
    map.put("authorities", authentication.getAuthorities());
    return map;
  }
}
