package com.onit_be.onit_be.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column
    private String nickName;

    @Column
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    // profileImg는 null 값 입력가능
    @Column
    private String profileImg;

    // 일반 회원가입인 경우 kakaoId는 null
    @Column(unique = true)
    private Long kakaoId;

    //수정 한 부분 . 일반 회원 가입 시에는  USER 로 ROLE 등록
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Plan> planList = new ArrayList<>();


//    @OneToMany(mappedBy = "user")
//    @JoinColumn
//    private List<Plan> planList = new ArrayList<>();


    User(Builder builder) {
        this.username = builder.username;
        this.nickName = builder.nickname;
        this.password = builder.password;
        this.profileImg = builder.profileImg;
        this.kakaoId = builder.kakaoId;
    }

    public User(String username, String password, String nickName, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.userRole = role;
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

    public void updateNicknameAndProfileImg(String nickname, String imgUrl) {
        this.nickName = nickname;
        this.profileImg = imgUrl;
    }

    public void updateNickname(String nickname) {
        this.nickName = nickname;
    }

    public User(String username, String password, String nickName) {
        this.username = username;
        this.password = password;
        this.nickName = nickName;
    }

}
