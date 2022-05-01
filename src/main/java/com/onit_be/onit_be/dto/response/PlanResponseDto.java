package com.onit_be.onit_be.dto.response;

import com.onit_be.onit_be.entity.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PlanResponseDto {

    private Long planId;
    private LocalDateTime planDate;
    private Location locationDetail;
    private int status;






    public PlanResponseDto(Long planId, LocalDateTime planDate, Location locationDetail, int status) {


        this.planId = planId;
        this.planDate = planDate;
        this.locationDetail = locationDetail;
        this.status = status;
    }
}
