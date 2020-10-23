package pl.senti.todoapp.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = IllegalExceptionProcessing.class)
public class IllegalExceptionsControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handlerIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
