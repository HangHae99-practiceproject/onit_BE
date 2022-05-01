package com.onit_be.onit_be.entity;

import com.onit_be.onit_be.dto.request.PlanRequestDto;
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

    public Plan(PlanRequestDto planRequestDto, User user) {
        this.planName = planRequestDto.getPlanName();
        this.planDate = planRequestDto.getPlanDate();
        this.user = user;
        this.location = planRequestDto.getLocation();
    }
}
