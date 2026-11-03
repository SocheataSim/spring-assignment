package co.istad.restfulemployeeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.accept.MediaTypeFileExtensionResolver;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice

public class CustomExceptionHandling {

    private final MediaTypeFileExtensionResolver mediaTypeFileExtensionResolver;

    public CustomExceptionHandling(MediaTypeFileExtensionResolver mediaTypeFileExtensionResolver) {
        this.mediaTypeFileExtensionResolver = mediaTypeFileExtensionResolver;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //catch message in dto
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleApiValidation(MethodArgumentNotValidException e){
        List<FieldErrorResponse> errors = new ArrayList<>();
        //getBindingResult: bind from JSON to Java Project
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError->
                        errors.add(
                              FieldErrorResponse.builder()
                                      .field(fieldError.getField())
                                      .message(fieldError.getDefaultMessage())
                                      .build()
                        ));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(Boolean.FALSE)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation Failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return errorResponse;

    }

}
