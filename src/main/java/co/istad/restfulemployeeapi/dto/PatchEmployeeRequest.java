package co.istad.restfulemployeeapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PatchEmployeeRequest(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Email(message = "Invalid Email Format")
        String email,

        @Positive(message = "Salary must be greater than 0")
        BigDecimal salary
) {
}
