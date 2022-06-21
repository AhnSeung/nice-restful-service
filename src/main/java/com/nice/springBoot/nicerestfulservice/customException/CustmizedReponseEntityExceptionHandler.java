package com.nice.springBoot.nicerestfulservice.customException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class CustmizedReponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    //RestController에서 발생한 모든 기본예외는 여기로 넘어와서 처리된다
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handlerAllException(Exception ex, WebRequest wr){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                                                    wr.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //userNotFoundException 새로 추가, HttpStatus = 404
    //UserNotFoundException 발생 시에는 여기서 처리함
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handlerUserNotFoundException(Exception ex, WebRequest wr){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                wr.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}
