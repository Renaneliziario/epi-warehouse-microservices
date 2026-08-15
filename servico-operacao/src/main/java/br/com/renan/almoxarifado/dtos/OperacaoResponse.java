package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.Operacao;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoResponse {

    private long id;
    private String nome;
    private String descricao;
    private long setorId;
    private LocalDateTime registeredAt;

    public static OperacaoResponse from(Operacao operacao) {
        return new OperacaoResponse(operacao.getId(), operacao.getNome(), operacao.getDescricao(),
                operacao.getSetorId(), operacao.getRegisteredAt());
    }
}
