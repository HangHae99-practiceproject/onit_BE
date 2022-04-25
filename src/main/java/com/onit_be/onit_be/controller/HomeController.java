package com.onit_be.onit_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {


    //메인 페이지
    @GetMapping("/home")
    public String home() {

        return "/index.html";

    }
}
