package co.istad.restfulemployeeapi.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid Email Format")
        String email,

        @Positive(message = "Salary must be greater than 0")
        @NotNull(message = "Salary is required")
        BigDecimal salary,

//        @NotBlank(message = "Hire date is required\n Format: YYYY-MM-DD")
        @NotNull(message = "Hire Date is required")
        @PastOrPresent(message = "Hire date can't be in the future")
        LocalDate hireDate

) {
}
