package com.onit.onit_be.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecordResDto {

    private Long planId;
    private String planName;
    private String planDate;
    private String address;
    private String penalty;
    private String url;

    public RecordResDto(Long planId, String planName, String planDateCv, String address, String penalty, String url) {
        this.planId = planId;
        this.planName = planName;
        this.planDate = planDateCv;
        this.address = address;
        this.penalty = penalty;
        this.url = url;
    }
}
