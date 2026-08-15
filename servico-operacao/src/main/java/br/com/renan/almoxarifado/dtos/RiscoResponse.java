package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.Risco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiscoResponse {

    private long id;
    private String nome;
    private String descricao;
    private String categoria;

    public static RiscoResponse from(Risco risco) {
        return new RiscoResponse(risco.getId(), risco.getNome(), risco.getDescricao(), risco.getCategoria());
    }
}
