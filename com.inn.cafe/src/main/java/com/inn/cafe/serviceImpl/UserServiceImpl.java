package com.inn.cafe.serviceImpl;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.UserRepository;
import com.inn.cafe.dto.UserResponseDto;
import com.inn.cafe.pojo.User;
import com.inn.cafe.service.UserService;
import com.inn.cafe.transformer.UserTransformer;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j   // for the logging purpose
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;


    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside the SignUp {}",requestMap);

        try {
            if(validateSignUpMap(requestMap)){
                User newUser = userRepository.findByEmailId(requestMap.get("email"));
                // if the user is null , means user with this email ,not present in db , then save this user
                if(Objects.isNull(newUser)){
                    // convert the requestMap to user Response Dto and then save that
                    UserResponseDto userResponseDto = UserTransformer.userRequestMapToUserResponseDto(requestMap);

                    // convert this user response Dto to user
                    User savedUser = UserTransformer.userResponseDtoToUser(userResponseDto);

                    // save the user
                    userRepository.save(savedUser);

                    return new ResponseEntity<>("Successfully Registered",HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("Email with this user Already Exists.",HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // validate the signup
    private boolean validateSignUpMap(Map<String, String> requestMap){
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }
}
