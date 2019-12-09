package com.codingliu.controller;

import com.alibaba.fastjson.JSONObject;
import com.codingliu.config.GeneralConfig;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * 作者：ltc
 * 时间：2019/12/06
 */
@Controller
public class GeneralController {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private GeneralConfig config;

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/oauth/redirect")
  public String getOAuthToken(@RequestParam("code") String code) {
    String url = "https://github.com/login/oauth/access_token";
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", config.getClientId());
    map.add("client_secret", config.getClientSecret());
    map.add("code", code);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    JSONObject result = restTemplate.postForObject(url, request, JSONObject.class);
    if (request != null) {
      return "forward:/info?token=" + result.getString("access_token");
    } else {
      return "redirect:/";
    }
  }

  @GetMapping("/info")
  @ResponseBody
  public String getUsername(@RequestParam("token") String token) {
    String url = "https://api.github.com/user";
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    headers.set("Authorization", "token " + token);
    HttpEntity request = new HttpEntity(headers);
    ResponseEntity<JSONObject> result = restTemplate.exchange(url, HttpMethod.GET, request, JSONObject.class);
    if(result.getStatusCode() == HttpStatus.OK) {
      return result.getBody().getString("name");
    }
    return null;
  }
}
