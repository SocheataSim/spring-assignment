package co.istad.restfulemployeeapi.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateEmployeeRequest(
        @NotBlank(message = "Name is required")
        @Size(min=2, max=100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid Email Format")
        String email,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be greater than 0")
        BigDecimal salary,

        @NotNull(message = "Hire date is required")
        @PastOrPresent(message = "HireDate must be in present or past.")
        LocalDate hireDate,

        @NotNull(message = "Active Status is required")
        Boolean isActive

) {
}
