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
public class SetorRequest {

    @NotBlank(message = "nome é obrigatório")
    private String nome;

    private String descricao;
}
