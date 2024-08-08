package com.api.orders.exception.common;

import com.api.orders.dto.ErrorDTO;
import com.api.orders.exception.RequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseController {


    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class.getName());

    /*@ExceptionHandler(value = {OrdersException.class})
    public ResponseEntity<ExceptionDetails> handleOrderException(OrdersException exception){
        LOG.error(exception.getMessage(), exception.getCause());
        return new ResponseEntity<>(exception.getDetails(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ExceptionDetails> handleOrderException(Throwable exception){
        LOG.error(exception.getMessage(), exception);
        var details = new ExceptionDetails("Ha ocurrido un error inesperado, por favor contacte el administrador", "error");
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDTO> runtimeExceptionHandler(RuntimeException ex){
        ErrorDTO error = ErrorDTO.builder()
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> requestExceptionHandler(RequestException ex){
        ErrorDTO error = ErrorDTO.builder()
                .status(ex.getStatus())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, ex.getStatus());
    }
}
