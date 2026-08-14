package br.com.renan.almoxarifado.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpiRequest {

    @NotBlank(message = "name e obrigatorio")
    private String name;

    @NotBlank(message = "description e obrigatorio")
    private String description;

    @NotBlank(message = "documentUrl e obrigatorio")
    private String documentUrl;

    @NotNull(message = "epiCategoryId e obrigatorio")
    private Long epiCategoryId;

    @NotNull(message = "currentStock e obrigatorio")
    @Min(value = 0, message = "currentStock nao pode ser negativo")
    private Integer currentStock;
}
