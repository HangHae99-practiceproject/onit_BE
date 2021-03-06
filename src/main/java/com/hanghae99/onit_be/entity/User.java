package com.hanghae99.onit_be.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tbl_user")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Lob
    @Column(nullable = false)
    private String profileImg;

    // 일반 회원가입인 경우 kakaoId는 null
    @Column(unique = true)
    private Long kakaoId;

    //수정 한 부분 . 일반 회원 가입 시에는  USER 로 ROLE 등록
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;
    
    //알림확인 여부
    @Column(name = "isNoticeAllowed", nullable = false)
    private boolean isNoticeAllowed;
    //fcm 토큰
    @Column
    private String token;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Plan> planList = new ArrayList<>();


    public User(Builder builder) {
        this.username = builder.username;
        this.nickname = builder.nickname;
        this.password = builder.password;
        this.profileImg = builder.profileImg;
        this.kakaoId = builder.kakaoId;
        this.isNoticeAllowed = false;
    }

    public User(String username, String password, String nickname, UserRoleEnum role, String profileImg) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userRole = role;
        this.profileImg = profileImg;
    }

    // 프로필 이미지 수정 시, 필요한 생성자
    public User(Long id, String imageUrl) {
        this.id = id;
        this.profileImg = imageUrl;
    }

    public User(String token) {
        this.token = token;
    }

    public User(Long kakaoId, String nickName, String encodedPassword, String profileImg) {
        this.kakaoId = kakaoId;
        this.username = nickName;
        this.nickname = nickName;
        this.password = encodedPassword;
        this.profileImg = profileImg;
        this.userRole = UserRoleEnum.USER;
        this.isNoticeAllowed = false;
    }

    // 이미지 수정 메서드
    public void update(Long id, String imageKey) {
        this.id = id;
        this.profileImg = imageKey;
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public static class Builder {
        private String username;
        private String nickname;
        private String password;
        private String profileImg;
        private Long kakaoId;

        // 필수적인 필드들
        public Builder(String username, String nickname, String password) {
            this.username = username;
            this.nickname = nickname;
            this.password = password;
        }

        // 선택적인 필드들(null값 들어올수 있음)
        public Builder profileImg(String profileImg) {
            this.profileImg = profileImg;
            return this;
        }

        public Builder kakaoId(Long kakaoId) {
            this.kakaoId = kakaoId;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    // 알림 여부 확인 메서드 true
    public void setNoticeAllowedTrue() {
        this.isNoticeAllowed = true;
    }

    // 알림 여부 확인 메서드 false
    public void setNoticeAllowedFalse() {
        this.isNoticeAllowed = false;
    }

}
