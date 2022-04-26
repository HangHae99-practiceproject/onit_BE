package com.onit_be.onit_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String username;
    private String nickname;
    private String profileImg;
}
