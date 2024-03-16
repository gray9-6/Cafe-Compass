package com.inn.cafe.restImpl;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.pojo.JWTResponse;
import com.inn.cafe.rest.UserRest;
import com.inn.cafe.service.UserService;
import com.inn.cafe.transformer.JwtResponseTransformer;
import com.inn.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {  // requestMap contains the user information
       try {
           return userService.signUp(requestMap);
       }catch (Exception e){
           e.printStackTrace();
       }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
//    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(Map<String, String> requestMap) {
        try {
            return userService.logIn(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return CafeUtils.getJWTResponseEntity(requestMap.get("email"),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
