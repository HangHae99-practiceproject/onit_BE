package com.onit_be.onit_be.controller;

import com.onit_be.onit_be.dto.request.PlanRequestDto;
import com.onit_be.onit_be.dto.response.PlanListResponseDto;
import com.onit_be.onit_be.dto.response.PlanResponseDto;
import com.onit_be.onit_be.security.UserDetailsImpl;
import com.onit_be.onit_be.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    //일정 만들기
    @PostMapping("/member/plan")
    public void createPlan(@RequestBody PlanRequestDto planRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        planService.createPlan(planRequestDto,userDetails.getUser());
    }

    //마이페이지 .List  username 으로 식별 .
    //1. 오늘 날짜 (구분 값 Y  = 오늘 일정이 있다 . N 은 없다 .  시간 까지 구별 .
    @GetMapping("/member/list/{userid}/{pageno}")
    public PlanListResponseDto getPlanList(@PathVariable Long userid,@PathVariable int pageno){
        PlanListResponseDto planListResponseDto = new PlanListResponseDto(planService.getPlanList(userid,pageno -1));
        return planListResponseDto;
    }
}
