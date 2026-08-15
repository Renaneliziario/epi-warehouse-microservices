package br.com.renan.almoxarifado.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Espelho local (client-side) do EpiResponse do servico-retirada-epi.
 * So os campos que o servico-operacao precisa exibir, deserializado
 * via Jackson a partir da resposta JSON de GET /epis/{id}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpiInfo {

    private long id;
    private String name;
    private String description;
}
