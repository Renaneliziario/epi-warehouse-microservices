package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.Employee;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private long id;
    private String name;
    private String email;
    private LocalDateTime registeredAt;

    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getRegisteredAt()
        );
    }
}
