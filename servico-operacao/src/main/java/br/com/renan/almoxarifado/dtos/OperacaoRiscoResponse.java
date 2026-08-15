package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.OperacaoRisco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRiscoResponse {

    private long id;
    private long operacaoId;
    private long riscoId;
    private String nivelRisco;
    private String observacao;

    public static OperacaoRiscoResponse from(OperacaoRisco operacaoRisco) {
        return new OperacaoRiscoResponse(operacaoRisco.getId(), operacaoRisco.getOperacaoId(),
                operacaoRisco.getRiscoId(), operacaoRisco.getNivelRisco(), operacaoRisco.getObservacao());
    }
}
