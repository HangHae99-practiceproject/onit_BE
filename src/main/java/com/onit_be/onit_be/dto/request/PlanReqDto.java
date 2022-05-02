package com.onit_be.onit_be.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onit_be.onit_be.entity.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class PlanReqDto {
    private String planName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planDate;
    private Location location;
    private String penalty;

}
