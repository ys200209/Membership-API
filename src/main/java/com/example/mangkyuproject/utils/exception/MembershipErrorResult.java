package com.example.mangkyuproject.utils.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {

    NOT_MEMBERSHIP_OWNER(HttpStatus.BAD_REQUEST, "Not a membership owner"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not found"),
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    INVALID_NUMBER_RANGE(HttpStatus.BAD_REQUEST, "Invalid Number Range"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public String getName() {
        return name();
    }
}
