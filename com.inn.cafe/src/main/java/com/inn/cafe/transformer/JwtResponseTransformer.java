package com.inn.cafe.transformer;

import com.inn.cafe.pojo.JWTResponse;

public class JwtResponseTransformer {

    public static JWTResponse generateJWTResponse(String jwtToken,String userName){
        return JWTResponse.builder()
                .jwtToken(jwtToken)
                .userName(userName)
                .build();
    }
}
