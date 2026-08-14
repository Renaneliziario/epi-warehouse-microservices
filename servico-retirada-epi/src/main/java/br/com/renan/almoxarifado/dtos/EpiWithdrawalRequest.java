package br.com.renan.almoxarifado.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpiWithdrawalRequest {

    @NotNull(message = "epiId e obrigatorio")
    private Long epiId;

    @NotNull(message = "employeeId e obrigatorio")
    private Long employeeId;

    @NotNull(message = "quantity e obrigatorio")
    @Min(value = 1, message = "quantity deve ser no minimo 1")
    private Integer quantity;
}
