package com.inn.cafe.utils;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.pojo.JWTResponse;
import com.inn.cafe.transformer.JwtResponseTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/*
* This is the utility class , which provides the utility method,
* which can be called in any part of the code*/
public class CafeUtils {

    private CafeUtils(){   // private constructor , so no one can make object of this class

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage,HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }

    public static ResponseEntity<JWTResponse> getJWTResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<JWTResponse>(JwtResponseTransformer.generateJWTResponse(CafeConstants.SOMETHING_WENT_WRONG,responseMessage), httpStatus);
    }
}
