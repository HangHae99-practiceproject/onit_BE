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

    //일정을 수정,삭제 할때 권한을 작성자만 할 수 있게 식별하려고 컬럼을 추가했습니다 . 다른방법이 있으면 알려주세요!
    private String writer;
    //일정을 추가할때 패널티를 클라이언트로 받아오기로 했습니다 . 때문에 DB에 저장해놨다가 보여줘야 할꺼 같아서 컬럼을 추가했습니다~
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
