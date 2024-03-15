package com.inn.cafe.JWT;


/*
 * in this we are going to implement the spring security with
 * the JWT Token
 * * */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private JwtFilter jwtFilter;
    @Value("${auth.type:JWT}")
    private String authenticationType;




    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



//    @Bean
//    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
///*
//        if (authenticationType.equals("JWT")) {
//*/
//
//        httpSecurity.csrf().disable()
//                .cors().configurationSource(request -> {
//                    CorsConfiguration corsConfiguration = new CorsConfiguration();
//                    corsConfiguration.setMaxAge(10800L);
//                    corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
//                    corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Authorization", "X-Requested-With", "Content-Type", "Accept"));
//                    corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS", "DELETE"));
//                    corsConfiguration.setExposedHeaders(Collections.singletonList("Content-Disposition"));
//                    return corsConfiguration;
//                }).and()
//                .authorizeHttpRequests()
//                .requestMatchers("/rest/login/**", "/actuator/health","/rest/public/**","/rest/refresh/**").permitAll()
//                .anyRequest().authenticated();
//                    /*(requests) -> {
//                                try {
//                                    requests
//                                            .requestMatchers("/rest/login/**", "/actuator/health","/rest/public/**","/rest/refresh/**").permitAll()
//                                            .anyRequest().authenticated().and()
//                                            .exceptionHandling().and().sessionManagement()
//                                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                                } catch (Exception e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                    )//.a("/rest/login/signIn").permitAll()
//                    .formLogin((form) -> form
//                            .loginPage("/rest/login/signIn")
//                            .permitAll())
//                    .logout((logout) -> logout.permitAll());*/
//        //.antMatchers("/rest/login/signUp").permitAll()
//                    /*.antMatchers("/rest/login/**").permitAll()
//                    .antMatchers("/actuator/health").permitAll()
//                    .antMatchers("/rest/public/**").permitAll()
//                    .antMatchers("/rest/refresh/**").permitAll()
//                    .anyRequest().authenticated()*//*.and()
//                    .exceptionHandling().and().sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
//        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
///*
//        }*/
//    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{

        // csrf - cross site request forgery
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/user/login","/user/signup","/user/forgotPassword")  // by pass
                .permitAll()
//                .requestMatchers("/student/**")
//                .hasAnyRole("STUDENT","ADMIN")
//                .requestMatchers("/admin/**")
//                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and().exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .formLogin();


        // now we have to add Filter on the JWT Token
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
