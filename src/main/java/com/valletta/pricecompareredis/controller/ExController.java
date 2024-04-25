package com.valletta.pricecompareredis.controller;

import com.valletta.pricecompareredis.vo.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExController {

//    @ExceptionHandler({Exception.class})
//    public ResponseEntity<Object> EveryException(final Exception ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> NotFountExceptionResponse(NotFoundException ex) {
        return new ResponseEntity<>(ex.getErrmsg(), ex.getHttpStatus());
    }
}
