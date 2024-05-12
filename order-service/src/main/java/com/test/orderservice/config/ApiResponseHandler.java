package com.test.orderservice.config;

import com.test.orderservice.dto.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    public <T extends RuntimeException> ResponseEntity<CommonResponse<String>> handleApiRequestException(final T e) {

        log.info("**ApiExceptionHandler controller, handle API request*\n");
        final var status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                CommonResponse.<String>builder()
                        .data( e.getMessage() )
                        .message( e.getMessage() )
                        .code(status.value())
                        .build(), status);
    }
}