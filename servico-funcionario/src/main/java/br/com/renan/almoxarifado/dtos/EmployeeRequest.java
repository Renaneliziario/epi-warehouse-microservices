package br.com.renan.almoxarifado.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "nome é obrigatório")
    private String name;

    @NotBlank(message = "email é obrigatório")
    @Email(message = "Email Inválido")
    private String email;
}
