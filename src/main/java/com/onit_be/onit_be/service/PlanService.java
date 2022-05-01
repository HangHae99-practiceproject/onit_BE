package com.onit_be.onit_be.service;

import com.onit_be.onit_be.dto.request.PlanRequestDto;
import com.onit_be.onit_be.dto.response.LocationDetail;
import com.onit_be.onit_be.dto.response.PlanListResponseDto;
import com.onit_be.onit_be.dto.response.PlanResponseDto;
import com.onit_be.onit_be.entity.Location;
import com.onit_be.onit_be.entity.Plan;
import com.onit_be.onit_be.entity.User;
import com.onit_be.onit_be.repository.PlanRepository;
import com.onit_be.onit_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    //Plan 만들기 .
    public void createPlan(PlanRequestDto planRequestDto, User user) {
        Plan plan = new Plan(planRequestDto,user);
        planRepository.save(plan);
    }


    //Plan List ( 약속 날짜 기준으로 가까운 순으로 정렬 )
    public  Page<PlanResponseDto> getPlanList(Long user_id,int pageno) {
        //현재 user Id 로 식별한 모든 List .
        List<Plan> planList =  planRepository.findAllByUserOrderByPlanDateAsc(userRepository.findById(user_id).orElseThrow(IllegalAccessError::new));
        Pageable pageable = getPageable(pageno);
        List<PlanResponseDto> planResponseDtoList = new ArrayList<>();
        forPlanList(planList,planResponseDtoList);

        int start = pageno * 5;
        int end = Math.min((start + 5), planList.size());

        Page<PlanResponseDto> page = new PageImpl<>(planResponseDtoList.subList(start, end), pageable, planResponseDtoList.size());
        return page;
    }

    //page sort 해주는 메서드 (어떻게 정렬 할 지는 front 와 논의 )
    private Pageable getPageable(int page) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "id");
        return PageRequest.of(page, 5, sort);
    }

    //일정 리스트 만들기 .
    private void forPlanList(List<Plan> planList, List<PlanResponseDto> planResponseDtoList) {
        for (Plan plan : planList) {
            int status = 0;
            LocalDateTime planDate = plan.getPlanDate();
            //현재 서울 날짜의 서울 시간이 panDate 보다 이전이다 . (미래)
            if(LocalDateTime.now(ZoneId.of("Asia/Seoul")).isBefore(planDate)){
                status = 1;
            }
            // 현재 서울 날짜의 서울 시간이 planDate 보다 이후 이다 . (과거)
            if(LocalDateTime.now(ZoneId.of("Asia/Seoul")).isAfter(planDate)){
                status = -1;
            }
            // 현재 서울 날짜의 서울 시간이 planDate 보다 이후 이다 . (오늘) 만약. 오늘 등록한 일정이 3 개라면 ? 첫 번째 index 값이 가장 가까운 일정이다 .
            // 약속 시간을 기준으로 정렬 해 놓았기 때문에 (오름차순)
            if(LocalDateTime.now(ZoneId.of("Asia/Seoul")).isBefore(planDate)){
                status = 0;
            }
            Long planId = plan.getId();
            Location locationDetail = plan.getLocation();

            PlanResponseDto planResponseDto = new PlanResponseDto(planId, planDate, locationDetail, status);
            planResponseDtoList.add(planResponseDto);
        }
    }

}
