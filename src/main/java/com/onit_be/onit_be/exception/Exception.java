package com.onit_be.onit_be.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Exception {
    private String msg;
    private HttpStatus httpStatus;
}
