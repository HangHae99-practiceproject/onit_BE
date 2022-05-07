package com.onit_be.onit_be.dto.response;

import com.onit_be.onit_be.entity.Location;
import com.onit_be.onit_be.entity.Plan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PlanResDto implements Serializable {

    private Long planId;
    private LocalDateTime planDate;
    private Location locationDetail;
    private int status;
    private boolean writer;

    public PlanResDto(Long planId, LocalDateTime planDate, Location locationDetail, int status, boolean result) {
        this.planId = planId;
        this.planDate = planDate;
        this.locationDetail = locationDetail;
        this.status = status;
        this.writer = result;
    }
}
