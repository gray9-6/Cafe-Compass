package com.inn.cafe.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    // we have to define the secret key , and on the basis of that , our json web token is generated
    private String secret = "#@incorrect";


    // extract the claims from the jwt token
    public <T> T extractClaims(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /*---------------------------------------------------------------------------------------------------------------*/


    // we are going to extract the username from this token
    public String extractUserName(String token){
        return extractClaims(token,Claims ::getSubject);
    }

    // we are going to extract the expiration  date of the jwt token
    public Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }

    // check if  our token which user has provided is expired or not
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /*---------------------------------------------------------------------------------------------------------------*/


    // create the token
    private String createToken(Map<String, Object> claims,String userName){
        return Jwts.builder()
                .setClaims(claims)  // set the claims
                .setSubject(userName)  // set the userName, here subject means userName
                .setIssuedAt(new Date(System.currentTimeMillis()))  // set the date , at which the token is issued
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // the calculation is to calculate the expiration time in hours(here it is for 10 hours)
                .signWith(SignatureAlgorithm.HS256,secret) // we are encrypting or secret key with HS256 encryption
                .compact();   // to generate the token
    }

    // generate the token
    public String generateToken(String userName,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        return createToken(claims,userName);
    }

    // validate the token
    public Boolean validateToken(String token, UserDetails userDetails){
        String userName = extractUserName(token);  // get the userName from the token
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
