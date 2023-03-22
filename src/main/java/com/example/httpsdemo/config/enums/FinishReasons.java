package com.example.httpsdemo.config.enums;

/**
 * project : httpsDemo
 * <p>description:
 *
 * @author : consi
 * @since : 2023/3/22
 **/
public enum FinishReasons {
  STOP("stop");

  final String desc;

  FinishReasons(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return this.desc;
  }
}
