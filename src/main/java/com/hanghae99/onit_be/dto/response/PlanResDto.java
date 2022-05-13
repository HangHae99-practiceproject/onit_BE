package com.hanghae99.onit_be.dto.response;

import com.hanghae99.onit_be.entity.Location;
import com.hanghae99.onit_be.entity.Plan;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PlanResDto {

    private Long planId;
    private String planName;
    private LocalDateTime planDate;
    private Location locationDetail;
    private int status;
    private boolean isMember;
    private String url;
    private String penalty;
    private String writer;

//    public PlanResDto(Long planId, String planName, LocalDateTime planDate, Location locationDetail, int status, boolean result, String url, String penalty, String writer) {
//    this.planId = planId;
//    this.planName = planName;
//    this.planDate = planDate;
//    this.locationDetail = locationDetail;
//    this.status = status;
//    this.writer = writer;
//    this.isMember = result;
//    this.url = url;
//    this.penalty = penalty;
//    }

    public PlanResDto(Long planId, String planName, LocalDateTime planDate) {
        this.planId = planId;
        this.planName = planName;
        this.planDate = planDate;
    }

    public PlanResDto(Long planId, String planName, LocalDateTime planDate, Location locationDetail, int status, String url, String penalty, String writer, boolean isMember) {
        this.planId = planId;
        this.planName = planName;
        this.planDate = planDate;
        this.locationDetail = locationDetail;
        this.status = status;
        this.writer = writer;
        this.isMember = isMember;
        this.url = url;
        this.penalty = penalty;
    }
}
