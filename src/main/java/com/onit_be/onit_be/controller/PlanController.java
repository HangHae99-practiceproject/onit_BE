package com.onit_be.onit_be.controller;

import com.onit_be.onit_be.aop.LogExecutionTime;

import com.onit_be.onit_be.dto.request.PlanReqDto;
import com.onit_be.onit_be.dto.response.PlanListResDto;
import com.onit_be.onit_be.security.UserDetailsImpl;
import com.onit_be.onit_be.service.PlanService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    //일정 만들기
    //와이어 프레임 상 클라이언트가 벌칙을 선택하고 서버에서 받아 그대로 저장 했다가 다시 보여주는 형식 같기에 plan 테이블에 패널티 컬럼이 추가 되어야 할꺼 같다 .
    //모모에서는 url 도 저장 하던데 이걸 어떻게 쓰는건지 ?
    @LogExecutionTime
    @PostMapping("/member/plan")
    public void createPlan(@RequestBody PlanReqDto planRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        planService.createPlan(planRequestDto,userDetails.getUser());
    }

    //마이페이지 .List  username 으로 식별 .
    //페이징 처리 , (과거 ,미래 , 오늘 ) 로 리스트가 나가며
    @GetMapping("/member/list/{userid}/{pageno}")
    @LogExecutionTime
    public PlanListResDto getPlanList(@PathVariable Long userid, @PathVariable int pageno){
        return new PlanListResDto(planService.getPlanList(userid,pageno -1));
    }

    //나의 일정 수정하기 (일정을 만든 사람만 .)
    //수정에서 리스폰스 값이 필요한지 얘기 .
    @PutMapping("/member/list/{planid}")
    @LogExecutionTime
    public void editPlan(@PathVariable Long planid, @RequestBody PlanReqDto planRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
         planService.editPlan(planid,planRequestDto,userDetails);
    }

    //일정 삭제
    //수정과 삭제를 하다보니 처음에 만들어진 일정에 대해서는 plan을 만든 사람만 할 수 있게 하자고 했었는데 그렇다면 plan 에 writer 컬럼이 추가되어야 하는것은
    //아닌지?
    @DeleteMapping("/member/list/{planid}")
    public void deletePlan(@PathVariable Long planid,@AuthenticationPrincipal UserDetailsImpl userDetails){
        planService.deletePlan(planid,userDetails);
    }


    //만남 장소 추천 어떻게 구현할까?
    //사용자의 위치를 기반으로 카페와 음식점을 보여준다 . == 프론트에서 너무 할일이 많아서 x
    //Onit에서 가장 많이 접선 장소로 선정된 10 개 씩을 보여준다 or 10개만 보여준다 category 없이 (데이터가 없을때는 더미데이터로 처리 )
    //전부다 더미로 조원끼리 요즘 가장 핫한 카페 10 곳 , 음식점 10 곳을 등록해 놨다가 보여준다 . (가장 쉬운 방법 같다 . )
    //위에꺼로 한다면 지역을 먼저 정하는게 가장 편할꺼 같다 .  다른 방법으로는  input 창을 만들고 지역 크게 서울,경기도,부산,제주도,등 으로 검색했을시에
    //미리 각 지역마다 10~20 (category 로 넣어뒀다가 보여준다 .)
    //현상 - 개인적으로는 location 테이블에서 추천하는 것이 아니라 장소추천테이블을 따로 만들어서 거기서 추천하는 방식도 괜찮을꺼 같습니다~!
    @GetMapping("/member/location")
    public void getLocationList(){

    }


}
