package com.inn.cafe.service;

import com.inn.cafe.pojo.JWTResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<JWTResponse> logIn(Map<String, String> requestMap);

}
