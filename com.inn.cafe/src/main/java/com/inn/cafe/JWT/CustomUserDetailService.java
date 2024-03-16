package com.inn.cafe.JWT;

import com.inn.cafe.dao.UserRepository;
import com.inn.cafe.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


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

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),new ArrayList<>());
    }

    public com.inn.cafe.pojo.User getUserDetail(){
        return user;
    }
}
