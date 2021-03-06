package com.hanghae99.onit_be.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.onit_be.user.UserRepository;
import com.hanghae99.onit_be.security.CustomLogoutSuccessHandler;
import com.hanghae99.onit_be.security.FilterSkipMatcher;
import com.hanghae99.onit_be.security.FormLoginFailHandler;
import com.hanghae99.onit_be.security.FormLoginSuccessHandler;
import com.hanghae99.onit_be.security.filter.FormLoginFilter;
import com.hanghae99.onit_be.security.filter.JwtAuthFilter;
import com.hanghae99.onit_be.security.jwt.HeaderTokenExtractor;
import com.hanghae99.onit_be.security.provider.FormLoginAuthProvider;
import com.hanghae99.onit_be.security.provider.JWTAuthProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final ObjectMapper objectMapper;
    private UserRepository userRepository;


    public WebSecurityConfig(
            JWTAuthProvider jwtAuthProvider,
            HeaderTokenExtractor headerTokenExtractor,
            ObjectMapper objectMapper,
            CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        this.jwtAuthProvider = jwtAuthProvider;
        this.headerTokenExtractor = headerTokenExtractor;
        this.objectMapper = objectMapper;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(formLoginAuthProvider())
                .authenticationProvider(jwtAuthProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/h2-console/**","/v3/api-docs","/favicon.ico",
                        "/swagger-resources/**", "/swagger-ui/", "/webjars/**", "/swagger/**","/swagger-ui/**","/notice");

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // httpBasic () 을 활성화 하면
        //http.httpBasic().disable();
        http.cors();
        http.csrf().disable();
        // h2-console 을 위한 설정을 추가
        http.headers().frameOptions().sameOrigin();

        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);//토크 기반이라 세션 사용 해제.
        /*
         * 1.
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */
        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("https://zsoon.shop/ws").permitAll()
                .antMatchers("ws/**").permitAll()
                .antMatchers("/users/kakao/**").permitAll()
                .anyRequest()
                .permitAll()
                .and()
                // [로그아웃 기능]
                .logout()
                // 로그아웃 요청 처리 URL
                .logoutUrl("/user/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .permitAll()
//                .and()
//                .exceptionHandling()
//                // "접근 불가" 페이지 URL 설정
//                .accessDeniedPage("/forbidden.html")
                .and()
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
        formLoginFilter.setFilterProcessesUrl("/user/login");
        formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
        formLoginFilter.setAuthenticationFailureHandler(formLoginFailHandler());
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler(userRepository);
    }

    @Bean
    public FormLoginFailHandler formLoginFailHandler(){
        return new FormLoginFailHandler();
    }


    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }

    private JwtAuthFilter jwtFilter() throws Exception {
        List<String> skipPathList = new ArrayList<>();
        // Static 정보 접근 허용
        skipPathList.add("GET,/images/**");
        skipPathList.add("GET,/css/**");

        // 회원 관리 API 허용
        skipPathList.add("GET,/user/**");
        skipPathList.add("POST,/user/signup");
        skipPathList.add("POST,/api/logout");
        skipPathList.add("GET,/users/kakao/callback");

        //게시글 조회
        skipPathList.add("GET,/api/post");
        skipPathList.add("GET,/api/post/{postId}");

        //skipPathList.add("GET,/home");

        skipPathList.add("GET,/basic.js");
        skipPathList.add("GET,/");
        //sse 메세지 test
        skipPathList.add("GET,/index.html");
        skipPathList.add("GET,/subscribe/**");

        //skipPathList.add("GET,/favicon.ico");
        skipPathList.add("POST,/api/idCheck");
        skipPathList.add("GET,/map/**");
        skipPathList.add("GET,/ws/**");
        skipPathList.add("POST,/ws/**");
        skipPathList.add("GET,/ws/**/**");
        skipPathList.add("GET,/health");


        FilterSkipMatcher matcher = new FilterSkipMatcher(
                skipPathList,
                "/**"
        );

        JwtAuthFilter filter = new JwtAuthFilter(
                matcher,
                headerTokenExtractor,
                objectMapper);
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}