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
public class EpiCategoryRequest {

    @NotBlank(message = "Nome é Obrigatório")
    private String name;

    @NotBlank(message = "Description é Obrigatório")
    private String description;
}
