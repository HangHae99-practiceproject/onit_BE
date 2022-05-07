package com.onit_be.onit_be.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name ="tbl_user_img")
public class UserImage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_img_id")
    private Long id;

    private String fileName;

    private String fileUrl;
}
