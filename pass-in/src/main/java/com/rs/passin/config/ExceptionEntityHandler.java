package com.rs.passin.config;

import com.rs.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.rs.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.rs.passin.domain.event.exceptions.EventFullException;
import com.rs.passin.domain.event.exceptions.EventNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class) //Indica qual a exceção o método lida
    public ResponseEntity handleEventNotFound(EventNotFoundException e){
        return ResponseEntity.notFound().build(); //Retorna 404
    }

    @ExceptionHandler(AttendeeNotFoundException.class) //Indica qual a exceção o método lida
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException e){
        return ResponseEntity.notFound().build(); //Retorna 404
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class) //Indica qual a exceção o método lida
    public ResponseEntity handleAttendeeAlreadyExists(AttendeeAlreadyExistException e){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EventFullException.class) //Indica qual a exceção o método lida
    public ResponseEntity handleEventIsFull(EventFullException e){
        return ResponseEntity.noContent().build();
    }
}
