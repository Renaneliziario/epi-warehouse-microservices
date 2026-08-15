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

    @NotNull(message = "epiId é obrigatório")
    private Long epiId;

    @NotNull(message = "employeeId é obrigatório")
    private Long employeeId;

    @NotNull(message = "quantity é obrigatório")
    @Min(value = 1, message = "quantity deve ser no mínimo 1")
    private Integer quantity;
}
