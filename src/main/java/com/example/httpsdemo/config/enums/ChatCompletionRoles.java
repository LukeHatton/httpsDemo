package com.example.httpsdemo.config.enums;

import lombok.Getter;

/**
 * project : httpsDemo
 * <p>description:
 * 表示在调用ChatCompletion模型时，用于对话模板的角色信息
 *
 * @author : consi
 * @since : 2023/3/15
 **/
@Getter
public enum ChatCompletionRoles {
  SYSTEM("system"),
  USER("user"),
  ASSISTANT("assistant");

  private final String role;

  ChatCompletionRoles(String role) {
    this.role = role;
  }

}
