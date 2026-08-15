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
public class OperacaoRequest {

    @NotBlank(message = "nome é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "setorId é obrigatório")
    private Long setorId;
}
