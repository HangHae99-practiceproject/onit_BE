package com.onit_be.onit_be.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.onit_be.onit_be.dto.IdCheckDto;
import com.onit_be.onit_be.dto.KakaoUserInfoDto;
import com.onit_be.onit_be.dto.LoginDto;
import com.onit_be.onit_be.dto.SignupRequestDto;
import com.onit_be.onit_be.entity.User;
import com.onit_be.onit_be.security.UserDetailsImpl;
import com.onit_be.onit_be.service.KakaoUserService;
import com.onit_be.onit_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService,KakaoUserService kakaoUserService) {

        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    // 회원 가입 요청 처리
    @PostMapping("/api/signup")
    public ResponseEntity<User> registerUser(@RequestBody SignupRequestDto requestDto) {

        User user = userService.registerUser(requestDto);
        return ResponseEntity.ok(user);
    }


    //아이디 중복 검사
    @PostMapping("/api/idCheck")
    public IdCheckDto vaildId(@RequestBody LoginDto requestDto) {

        return userService.vaildId(requestDto);
    }

    //로그인 여부 확인.
    @GetMapping("/api/islogin")
    public LoginDto checkLogin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        LoginDto loginDto = new LoginDto();

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 유저가 아닙니다. ");
        } else {
            loginDto.setUsername(userDetails.getUsername());
            return loginDto;
        }
    }

    // 카카오 로그인
    @GetMapping("/users/kakao/callback")
    public KakaoUserInfoDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoUserService.kakaoLogin(code, response);
    }

}