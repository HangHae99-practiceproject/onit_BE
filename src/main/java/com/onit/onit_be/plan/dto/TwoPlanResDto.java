package com.onit.onit_be.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TwoPlanResDto {

    private PlanListResDto.PlanListsResDto totalPlanList;

    private PlanListResDto.PlanListsResDto myPlanList;

    private PlanListResDto.PlanListsResDto invitedPlanList;


    public TwoPlanResDto(PlanListResDto.PlanListsResDto myPlanListsResDto,
                         PlanListResDto.PlanListsResDto invitedPlanListsResDto,
                         PlanListResDto.PlanListsResDto totalPlanList ) {
        this.myPlanList = myPlanListsResDto;
        this.invitedPlanList = invitedPlanListsResDto;
        this.totalPlanList = totalPlanList;
    }
}
