package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.OperacaoEpi;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoEpiResponse {

    private long id;
    private long operacaoId;
    private long epiId;
    private boolean obrigatorio;
    private String observacao;

    public static OperacaoEpiResponse from(OperacaoEpi operacaoEpi) {
        return new OperacaoEpiResponse(operacaoEpi.getId(), operacaoEpi.getOperacaoId(), operacaoEpi.getEpiId(),
                operacaoEpi.isObrigatorio(), operacaoEpi.getObservacao());
    }
}
