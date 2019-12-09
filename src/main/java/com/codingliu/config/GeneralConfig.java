package com.codingliu.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 作者：ltc
 * 时间：2019/12/06
 */
@Configuration
@Data
public class GeneralConfig {

  @Value("${clientId}")
  private String clientId;
  @Value("${clientSecret}")
  private String clientSecret;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
