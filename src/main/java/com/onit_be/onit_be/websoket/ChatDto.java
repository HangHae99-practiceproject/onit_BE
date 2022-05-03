package com.onit_be.onit_be.websoket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDto {
    private Long planId;
    private MessageType type;
    private String sender;
    private String content;
}
