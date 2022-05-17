package com.hanghae99.onit_be.plan;

import com.hanghae99.onit_be.aop.LogExecutionTime;
import com.hanghae99.onit_be.common.ResultDto;
import com.hanghae99.onit_be.plan.dto.PlanDetailResDto;
import com.hanghae99.onit_be.plan.dto.PlanListResDto;
import com.hanghae99.onit_be.plan.dto.PlanReqDto;
import com.hanghae99.onit_be.plan.dto.TwoPlanResDto;
import com.hanghae99.onit_be.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PlanController {

    private final PlanService planService;

    //일정 만들기
    //와이어 프레임 상 클라이언트가 벌칙을 선택하고 서버에서 받아 그대로 저장 했다가 다시 보여주는 형식 같기에 plan 테이블에 패널티 컬럼이 추가 되어야 할꺼 같다 .
    //모모에서는 url 도 저장 하던데 이걸 어떻게 쓰는건지 ?
    @LogExecutionTime
    @PostMapping("/member/plan")
    public ResponseEntity<ResultDto> createPlan (@RequestBody PlanReqDto planReqDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        planService.createPlan(planReqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ResultDto<>("일정 등록 성공!"));
    }

    // 일정 목록 조회 (내가 만든 )
    // 페이징 처리
//    @LogExecutionTime
//    @GetMapping("/member/list/{userId}/{pageno}")
//    public ResponseEntity<ResultDto<PlanListResDto>> getPlanList (@PathVariable Long userId,
//                                                                  @PathVariable int pageno,
//                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        PlanListResDto planListResDto = new PlanListResDto(planService.getPlanList(userId, pageno-1, userDetails.getUser()));
//        return ResponseEntity.ok().body(new ResultDto<>("일정 목록 조회 성공!", planListResDto));
//    }

    // 일정 목록 조회 (내가 만든 일정/초대된 일정)
    @LogExecutionTime
    @GetMapping("/member/plans/{pageno}")
    public TwoPlanResDto getPlansList (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable int pageno) {
        return planService.getPlansList(userDetails.getUser(),pageno-1);
    }

    // 일정 상세 조회 (내가 만든)
    @LogExecutionTime
    @GetMapping("/member/plan/{randomUrl}")
    public ResponseEntity<ResultDto<PlanDetailResDto>> getPlan (@PathVariable("randomUrl") String url,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        PlanDetailResDto planDetailResDto = planService.getPlan(url,userDetails.getUser());
        return ResponseEntity.ok().body(new ResultDto<>("일정 상세 조회 성공!", planDetailResDto));
    }

    // 일정 수정
    @LogExecutionTime
    @PutMapping("/member/plan/{randomUrl}")
    public ResponseEntity<ResultDto> editPlan (@PathVariable("randomUrl") String url, @RequestBody PlanReqDto planReqDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        planService.editPlan(url, planReqDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ResultDto("일정 수정 성공!"));
    }

    // 일정 삭제
    @LogExecutionTime
    @DeleteMapping("/member/plan/{randomUrl}")
    public ResponseEntity<ResultDto> deletePlan (@PathVariable("randomUrl") String url, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        planService.deletePlan(url, userDetails.getUser());
        return ResponseEntity.ok().body(new ResultDto("일정 삭제 성공!"));
    }

}

