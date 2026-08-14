package br.com.renan.almoxarifado.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class EpiCategoryClient {

    private final RestClient restClient;

    public EpiCategoryClient(@Value("${servico-categoria-epi.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public boolean exists(long epiCategoryId) {
        try {
            restClient.get()
                    .uri("/epi-categories/{id}/exists", epiCategoryId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        }
    }
}
