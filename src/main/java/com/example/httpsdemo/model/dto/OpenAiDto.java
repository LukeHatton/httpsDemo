package com.example.httpsdemo.model.dto;

import lombok.Data;

/**
 * <p>Project: httpsDemo
 * <p>Description:
 * <p>OpenAi前端交互模型对象
 *
 * @author lizhao 2023/3/9
 */
@Data
public class OpenAiDto {
  String objectId;

  String apiKey;

  String content;
}
