package com.hanghae99.onit_be.websocket.dto;

import com.hanghae99.onit_be.websocket.MessageType;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class MapDto {

    private Long planId;
    private String sender;
    private String profileImg;
    private String lat;
    private String lng;
    private MessageType type;
    private String destLat;
    private String destLng;
    private String distance;

    @Builder
    public MapDto(Long planId, String sender, String lat, String lng, String destLat, String destLng, MessageType type, String profileImg, String distance) {
        this.planId = planId;
        this.sender = sender;
        this.lat = lat;
        this.lng = lng;
        this.destLat = destLat;
        this.destLng = destLng;
        this.type = type;
        this.profileImg = profileImg;
        this.distance = distance;
    }


    public static MapDto from(EnterDto enterDto,String profileImg) {
        return MapDto.builder()
                .planId(enterDto.getPlanId())
                .sender(enterDto.getSender())
                .profileImg((profileImg))
                .lat(enterDto.getLat())
                .lng(enterDto.getLng())
                .type(MessageType.DEST)
                .build();
    }
}
