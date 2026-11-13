package co.istad.restfulemployeeapi.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees") //optional like define name, like all name is database is plural and lowercase

public class Employee {

//    private static int nextId = 3;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //standard one
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(
            nullable = false,
            check = @CheckConstraint(
                    constraint = "salary > 0"
            ),
            precision = 10,
            scale = 2
    )
    private BigDecimal salary;

    private LocalDate hireDate;

    private Boolean isActive;

//    public Employee(String name, String email, BigDecimal salary, LocalDate hireDate) {
//        this.id = nextId++;
//        this.name = name;
//        this.email = email;
//        this.salary = salary;
//        this.hireDate = hireDate;
//        this.isActive = Boolean.TRUE;
//    }
}

//precision: number of digits
//scale: number of digit after .
