package br.com.renan.almoxarifado.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRiscoRequest {

    @NotNull(message = "riscoId é obrigatório")
    private Long riscoId;

    @NotBlank(message = "nivelRisco é obrigatório")
    private String nivelRisco;

    private String observacao;
}
