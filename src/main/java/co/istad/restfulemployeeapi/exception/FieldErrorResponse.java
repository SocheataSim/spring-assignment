package co.istad.restfulemployeeapi.exception;

import lombok.Builder;

@Builder // to build object field error response
public record FieldErrorResponse(
        String field,
        String message
) {

}
