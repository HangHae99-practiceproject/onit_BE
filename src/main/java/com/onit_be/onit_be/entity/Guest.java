package com.onit_be.onit_be.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tbl_guest")
public class Guest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "guest_id")
    private Long id;

    @Column(nullable = false)
    private String ninkName;

//    @Column(nullable = false)
//    private String profileImg;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;
}
