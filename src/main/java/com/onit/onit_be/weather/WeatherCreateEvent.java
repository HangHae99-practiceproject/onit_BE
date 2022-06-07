package com.onit.onit_be.weather;

import com.onit.onit_be.entity.Plan;
import lombok.Getter;

@Getter
public class WeatherCreateEvent {

    private final Plan plan;

    public WeatherCreateEvent(Plan plan) {
        this.plan = plan;
    }
}
