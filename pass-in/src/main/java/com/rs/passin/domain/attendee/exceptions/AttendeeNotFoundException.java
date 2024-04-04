package com.rs.passin.domain.attendee.exceptions;

public class AttendeeNotFoundException extends RuntimeException{

    public AttendeeNotFoundException(String msg) {
        super(msg);
    }
}
