package com.inn.cafe.rest;


import com.inn.cafe.pojo.JWTResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody(required = true) Map<String, String> requestMap);
}
