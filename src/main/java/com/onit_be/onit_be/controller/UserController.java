package com.onit_be.onit_be.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.onit_be.onit_be.dto.*;
import com.onit_be.onit_be.dto.request.LoginReqDto;
import com.onit_be.onit_be.dto.request.SignupReqDto;
import com.onit_be.onit_be.dto.response.IdCheckResDto;
import com.onit_be.onit_be.dto.response.KakaoUserInfoResDto;
import com.onit_be.onit_be.entity.User;
import com.onit_be.onit_be.service.KakaoUserService;
import com.onit_be.onit_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/user/signup")
    public ResponseEntity<Object> registerUser(@RequestBody SignupReqDto requestDto) {
        User user = userService.registerUser(requestDto);
        return ResponseEntity.ok().body(user);
    }

    //아이디 중복 검사
    @PostMapping("/api/idCheck")
    public IdCheckResDto vaildId(@RequestBody LoginReqDto requestDto) {
        return userService.vaildId(requestDto);
    }

    //회원 정보
//    @GetMapping("/api/user/info")
//    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return userService.getUserInfo(userDetails);
//    }

   // 카카오 로그인
    @GetMapping("/users/kakao/callback")
    public KakaoUserInfoResDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoUserService.kakaoLogin(code, response);
    }

}