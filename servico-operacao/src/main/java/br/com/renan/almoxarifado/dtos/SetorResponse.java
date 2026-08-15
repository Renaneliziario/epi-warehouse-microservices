package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.Setor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetorResponse {

    private long id;
    private String nome;
    private String descricao;

    public static SetorResponse from(Setor setor) {
        return new SetorResponse(setor.getId(), setor.getNome(), setor.getDescricao());
    }
}
