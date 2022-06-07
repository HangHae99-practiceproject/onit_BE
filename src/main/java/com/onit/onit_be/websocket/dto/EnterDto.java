package com.onit.onit_be.websocket.dto;

import com.onit.onit_be.websocket.MessageType;
import lombok.Data;


@Data
public class EnterDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private String lat;
    private String lng;
}
