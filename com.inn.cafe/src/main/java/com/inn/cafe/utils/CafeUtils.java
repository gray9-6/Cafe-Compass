package com.inn.cafe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {

    private CafeUtils(){   // private constructor , so no one can make object of this class

    }


    public static ResponseEntity<String> getResponseEntity(String responseMessage,HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
