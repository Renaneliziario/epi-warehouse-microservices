package br.com.renan.almoxarifado.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class EpiClient {

    private final RestClient restClient;

    public EpiClient(@Value("${servico-retirada-epi.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public boolean exists(long epiId) {
        try {
            restClient.get()
                    .uri("/epis/{id}/exists", epiId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }

    public EpiInfo fetch(long epiId) {
        return restClient.get()
                .uri("/epis/{id}", epiId)
                .retrieve()
                .body(EpiInfo.class);
    }
}
