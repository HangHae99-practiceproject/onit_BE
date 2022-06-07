package com.onit.onit_be.noti.event;

import com.onit.onit_be.entity.Plan;
import com.onit.onit_be.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LeaveEvent {
    private final Plan plan;
    private final String message;
    private final User user;
}
