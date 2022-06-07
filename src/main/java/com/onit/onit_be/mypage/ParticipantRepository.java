package com.onit.onit_be.mypage;

import com.onit.onit_be.entity.Participant;
import com.onit.onit_be.entity.Plan;
import com.onit.onit_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByUserAndPlan(User user, Plan plan);

    // 일정 시간에 따른 조회. (일정 가까운 순으로 정렬되어 있다.)
    List<Participant> findAllByUserOrderByPlanDate(User user);

    void deleteByUserAndPlan(User user, Plan plan);

    // 참가자 list
    List<Participant> findAllByPlan(Plan plan);

    void deleteAllByUser(User user);
}
