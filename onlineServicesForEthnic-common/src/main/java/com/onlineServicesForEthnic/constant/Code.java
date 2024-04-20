package com.onlineServicesForEthnic.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Code {
    //-----硬件交互------
    Success(0x00),
    BadMsgID(0x01),
    UnknownMsgID(0x02),
    Timeout(0x03),
    Other(0xff),
    //-----------前端交互----------
    Fail(3800),
    OK(HttpStatus.OK.value()),
    Forbidden(FORBIDDEN.value()),
    UnAuthorization(UNAUTHORIZED.value()),
    BadRequest(BAD_REQUEST.value());

    private Integer code;
}
