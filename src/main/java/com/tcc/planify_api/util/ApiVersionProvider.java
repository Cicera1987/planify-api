package com.tcc.planify_api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiVersionProvider {
  @Value("${api.version}")
  private String version;

  public String getVersion() {
    return version;
  }
}
