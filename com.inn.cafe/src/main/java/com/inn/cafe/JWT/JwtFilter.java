package com.inn.cafe.JWT;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* ye class , ye ensure karegi ki , jab bhi user authenticate hone ke baad,kisi protected api ko hit kar raha hai ,
* toh ye usko class check karegi
* 1. kya wo client jo api ko hit kar raha hai , uske pass header mein token hai(wo token jo humne usko generate karke diya tha authentication ke time,
*      ki, jab kisi api ki jarurat ho, toh iss token ko lekar aana)
* 2. agar han uske pass token hai , toh kya wo sahi token hai(hum uss JWT token ko validate karenge, using secret key)
*
* Note:- ye class har baar chalegi jab bhi , user ke taraf se request aayegi. after being authenticated.
* */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtUtil jwtUtil;


    // iske paas hi loadUserByUsername method hai , toh jab bhi iske help se user aayega toh wo db se uthkar aayega
    @Autowired
    private CustomUserDetailService customUserDetailService;

    Claims claims = null;
    private String userName = null;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // if request is coming from this end point then let them simply pass
        if(httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else{
            // header se Authorization token nikal lo
            // ye kuch aisa aayega :- authorizationHeader = Bearer shsddhuduiewaskjdnshjfgfg
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String token = null;

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){  // token se info nikali
                token = authorizationHeader.substring(7);   // getting the token (sometimes Bearer , gets attached with the token , so remove that )
                userName = jwtUtil.extractUserName(token);  // extract user name
                claims = jwtUtil.extractAllClaims(token);  // extract all claims
            }

            // we have extracted the values, so now we authenticate the user
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){   // agar user successfully authenticate hua hai toh iss method mein jaayenge
                UserDetails userDetails = customUserDetailService.loadUserByUsername(userName); // user nikala

                if(jwtUtil.validateToken(token,userDetails)){
                    // set the authentication
                    // UsernamePasswordAuthenticationToken ye hume batayega ki banda authenticated hai ya nahi
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    // authentication humesa SecurityContextHolder mein set kiya jaata hai
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }

    public boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser(){
        return userName;
    }
}
