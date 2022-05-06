package com.onit_be.onit_be.repository;

import com.onit_be.onit_be.entity.Plan;
import com.onit_be.onit_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan,Long> {
    List<Plan> findAllByUserOrderByPlanDateAsc(User user);
    List<Plan> findAllByWriter(String writer);

    Optional<Plan> findPlanByUrl(String url);
}
