package com.onit_be.onit_be.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlanListResponseDto {
    private List<PlanResponseDto> planList;
    private int totalPage;
    private int currentPage;

    public PlanListResponseDto(Page<PlanResponseDto> page) {
        this.planList = page.getContent();
        this.totalPage = page.getTotalPages();
        this.currentPage = page.getNumber();
    }

}
