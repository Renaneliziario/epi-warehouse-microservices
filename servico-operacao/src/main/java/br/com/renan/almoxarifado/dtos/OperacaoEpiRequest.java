package br.com.renan.almoxarifado.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoEpiRequest {

    @NotNull(message = "epiId é obrigatório")
    private Long epiId;

    private boolean obrigatorio = true;

    private String observacao;
}
