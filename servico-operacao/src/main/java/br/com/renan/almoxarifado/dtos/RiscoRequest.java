package br.com.renan.almoxarifado.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiscoRequest {

    @NotBlank(message = "nome e obrigatorio")
    private String nome;

    private String descricao;

    @NotBlank(message = "categoria e obrigatoria")
    private String categoria;
}
