package com.onit_be.onit_be.websoket;

import lombok.Data;


@Data
public class EnterDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private String lat;
    private String lng;

}
