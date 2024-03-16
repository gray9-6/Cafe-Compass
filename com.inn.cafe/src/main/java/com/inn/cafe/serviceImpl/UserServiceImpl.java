package com.inn.cafe.serviceImpl;

import com.inn.cafe.JWT.CustomUserDetailService;
import com.inn.cafe.JWT.JwtUtil;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.UserRepository;
import com.inn.cafe.dto.UserResponseDto;
import com.inn.cafe.pojo.JWTResponse;
import com.inn.cafe.pojo.User;
import com.inn.cafe.service.UserService;
import com.inn.cafe.transformer.JwtResponseTransformer;
import com.inn.cafe.transformer.UserTransformer;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j   // for the logging purpose
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;  // to authenticate
    @Autowired
    CustomUserDetailService customUserDetailService;  // to fetch user info
    @Autowired
    JwtUtil jwtUtil;   // to create jwt token


    public static final String BAD_CREDENTIALS = "{\"message\":\"" + "Bad Credentials.";
    public static final String ADMIN_APPROVAL = "{\"message\":\"" + "Wait for admin Approval.";

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside the SignUp {}",requestMap);

        try {
            if(validateSignUpMap(requestMap)){
                User user = userRepository.findByEmailId(requestMap.get("email"));
//                if(optionalUser.isPresent()){
//                    throw new RuntimeException("User Already Exists");
//                }
                // if the user is null , means user with this email ,not present in db , then save this user
                if(user == null){
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

    // validate the signup (requestMap contains the client payload)
    private boolean validateSignUpMap(Map<String, String> requestMap){
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }


    @Override
    public ResponseEntity<JWTResponse> logIn(Map<String, String> requestMap) {
        log.info("Inside login method {}");
        try {

            User user = userRepository.findByEmailId(requestMap.get("email"));


            // get the authentication, ki banda authenticated hai ya nahi
            Authentication auth = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword())   // we have extracted userName and password from request Map
            );

            // authenticate the user request :- means whether the admin approves or not
            if(auth.isAuthenticated()){       // means we have valid authentication
                if(customUserDetailService.getUserDetail().getStatus().equalsIgnoreCase("true")){  // and the user is also approved
                    JWTResponse jwtResponse = JWTResponse.builder()
                            .jwtToken("{\"token\":\"" +
                                    jwtUtil.generateToken(customUserDetailService.getUserDetail().getName(),customUserDetailService.getUserDetail().getRole()) +
                                    "\"}")
                            .userName(customUserDetailService.getUserDetail().getName())
                            .build();
                    return new ResponseEntity<JWTResponse>(jwtResponse,HttpStatus.OK);
                }else{  // user is not approved
                    return new ResponseEntity<JWTResponse>(JwtResponseTransformer.generateJWTResponse(ADMIN_APPROVAL,customUserDetailService.getUserDetail().getName()),HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception e){
            log.info("{}",e);
        }


        return new ResponseEntity<JWTResponse>(JwtResponseTransformer.generateJWTResponse(BAD_CREDENTIALS,customUserDetailService.getUserDetail().getName()),HttpStatus.BAD_REQUEST);
    }

}
