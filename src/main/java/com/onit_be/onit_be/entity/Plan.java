package com.onit_be.onit_be.entity;

import com.onit_be.onit_be.dto.request.PlanReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_plan")
public class Plan extends TimeStamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "plan_id")
    private Long id;

    private String planName;

    private LocalDateTime planDate;

    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String writer;

    private String penalty;

    public Plan(PlanReqDto planRequestDto, User user) {
        this.planName = planRequestDto.getPlanName();
        this.planDate = planRequestDto.getPlanDate();
        this.user = user;
        this.location = planRequestDto.getLocation();
        this.writer = user.getUserNickname();
        this.penalty = planRequestDto.getPenalty();
    }

    public void update(PlanReqDto planRequestDto) {
        this.planName = planRequestDto.getPlanName();
        this.planDate = planRequestDto.getPlanDate();
        this.location = planRequestDto.getLocation();
    }
}
