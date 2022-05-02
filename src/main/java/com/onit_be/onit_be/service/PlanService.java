package com.onit_be.onit_be.service;

import com.onit_be.onit_be.dto.request.PlanReqDto;
import com.onit_be.onit_be.dto.response.PlanResDto;
import com.onit_be.onit_be.entity.Location;
import com.onit_be.onit_be.entity.Plan;
import com.onit_be.onit_be.entity.User;
import com.onit_be.onit_be.repository.PlanRepository;
import com.onit_be.onit_be.repository.UserRepository;
import com.onit_be.onit_be.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    //Plan 만들기 .
    @Transactional
    public void createPlan(PlanReqDto planRequestDto, User user) {
        // 1. 현재 로그인한 유저의 닉네임으로 등록된 모든 plan list 를 조회.
        List<Plan> planList = planRepository.findAllByWriter(user.getUserNickname());
        LocalDateTime today = planRequestDto.getPlanDate();
        for (Plan plan : planList) {
            //2.이중약속에 대한 처리 .
            //long remainHour = ChronoUnit.HOURS.between(plan.getPlanDate().toLocalTime(), planRequestDto.getPlanDate().toLocalTime());
            int comResult = compareDay(plan.getPlanDate(),today);
            if (comResult == 0) {
                long remainHour = ChronoUnit.HOURS.between(plan.getPlanDate().toLocalTime(), planRequestDto.getPlanDate().toLocalTime());
                System.out.println(remainHour);
                //3.이중약속이 아니면서 약속시간 기준 +- 2 에 해당하는 약속 x
                //ex) 6시 에 일정이 있으면 4시 부터 8시 사이에는 일정을 잡지 못하게 .
                if (!(remainHour > 2 || remainHour < -2))
                    throw new IllegalArgumentException("오늘 이미 일정이 있습니다! 이미 있는 일정 기준 +-2 시간 이외에 일정을 잡아주세요!");
            }
        }
        Plan plan = new Plan(planRequestDto, user);
        planRepository.save(plan);
    }

    //일 비교 .
    public static int compareDay(LocalDateTime date1, LocalDateTime date2) {
        LocalDateTime dayDate1 = date1.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime dayDate2 = date2.truncatedTo(ChronoUnit.DAYS);
        return dayDate1.compareTo(dayDate2);
    }
    //Plan List ( 약속 날짜 기준으로 가까운 순으로 정렬 )
//    @Cacheable(value="plan", key = "#id")
    public Page<PlanResDto> getPlanList(Long user_id, int pageno) {
        //현재 user Id 로 식별한 모든 List .
        List<Plan> planList = planRepository.findAllByUserOrderByPlanDateAsc(userRepository.findById(user_id).orElseThrow(IllegalArgumentException::new));
        Pageable pageable = getPageable(pageno);
        List<PlanResDto> planResponseDtoList = new ArrayList<>();
        forPlanList(planList, planResponseDtoList);

        int start = pageno * 5;
        int end = Math.min((start + 5), planList.size());

        Page<PlanResDto> page = new PageImpl<>(planResponseDtoList.subList(start, end), pageable, planResponseDtoList.size());
        return page;
    }

    //page sort 해주는 메서드 (어떻게 정렬 할 지는 front 와 논의 )
    private Pageable getPageable(int page) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "id");
        return PageRequest.of(page, 5, sort);
    }

    //일정 리스트 만들기 .(status 추가 지난일정 , 오늘일정 , 앞으로의 일정을 나누기 위해서 )

    private void forPlanList(List<Plan> planList, List<PlanResDto> planResponseDtoList) {
        for (Plan plan : planList) {
            int status = 0;
            LocalDateTime planDate = plan.getPlanDate();
            //현재 서울 날짜의 서울 시간이 panDate 보다 이전이다 . (미래)
            if (LocalDateTime.now(ZoneId.of("Asia/Seoul")).isBefore(planDate)) {
                status = 1;
            }
            // 현재 서울 날짜의 서울 시간이 planDate 보다 이후 이다 . (과거)
            if (LocalDateTime.now(ZoneId.of("Asia/Seoul")).isAfter(planDate)) {
                status = -1;
            }
            // 현재 서울 날짜의 서울 시간이 planDate 보다 이후 이다 . (오늘) 만약. 오늘 등록한 일정이 3 개라면 ? 첫 번째 index 값이 가장 가까운 일정이다 .
            // 약속 시간을 기준으로 정렬 해 놓았기 때문에 (오름차순)
            if (LocalDate.now(ZoneId.of("Asia/Seoul")).isEqual(ChronoLocalDate.from(planDate))) {
                status = 0;
            }
            Long planId = plan.getId();
            Location locationDetail = plan.getLocation();

            PlanResDto planResponseDto = new PlanResDto(planId, planDate, locationDetail, status);
            planResponseDtoList.add(planResponseDto);
        }
    }


    //처음에 기획할 때 일정을 수정하고 삭제할 수있는 사람은 일정을 만든사람 한명 뿐이라고 지정을 했다 .
    //때문에 일정 테이블에 writer 컬럼을 추가하고 writer 와 현재 로그인한 사람의 닉네임을 비교 후 일치 하지 않으면 수정과 삭제 권한이 없도록
    //예외 처리가 필요할 것 같다 .

    //일정 수정.
    @Transactional
    public void editPlan(Long planid, PlanReqDto planRequestDto, UserDetailsImpl userDetails) {
        Plan plan = planRepository.findById(planid).orElseThrow(IllegalArgumentException::new);
        plan.update(planRequestDto);

    }


    //일정 삭제 .
    public void deletePlan(Long planid, UserDetailsImpl userDetails) {
        planRepository.deleteById(planid);
    }


}
