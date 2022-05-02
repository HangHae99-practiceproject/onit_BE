package com.onit_be.onit_be.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Location {

    private String name;
    //위도
    private double lat;
    //경도
    private double lng;

    private String address;

}
