package com.inn.cafe.JWT;

import com.inn.cafe.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    private com.inn.cafe.pojo.User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername() ",username );

        user = userRepository.findByEmailId(username);
        if(user == null){
            throw new RuntimeException("User doesn't exist");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),new ArrayList<>());
    }

    public com.inn.cafe.pojo.User getUserDetail(){
        return user;
    }
}
