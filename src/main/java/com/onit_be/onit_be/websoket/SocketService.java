package com.onit_be.onit_be.websoket;

import com.onit_be.onit_be.entity.Plan;
import com.onit_be.onit_be.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlanRepository planRepository;

    public void setDestination(Long planId, MapDto mapDto) {
        Plan plan = planRepository.findById(planId).orElseThrow(IllegalArgumentException::new);
        mapDto.setDestLat(String.valueOf(plan.getLocation().getLat()));
        mapDto.setDestLng(String.valueOf(plan.getLocation().getLng()));
    }
}
