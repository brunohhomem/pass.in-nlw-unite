package com.rs.passin.config;

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
}
