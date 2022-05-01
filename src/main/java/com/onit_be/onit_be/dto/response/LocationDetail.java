package com.onit_be.onit_be.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDetail {
    private String locationName;
    private double x;
    private double y;
}
