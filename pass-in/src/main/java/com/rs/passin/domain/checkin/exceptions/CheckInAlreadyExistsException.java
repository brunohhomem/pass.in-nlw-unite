package com.rs.passin.domain.checkin.exceptions;

public class CheckInAlreadyExistsException extends RuntimeException{

    public CheckInAlreadyExistsException(String msg){
        super(msg);
    }

}
