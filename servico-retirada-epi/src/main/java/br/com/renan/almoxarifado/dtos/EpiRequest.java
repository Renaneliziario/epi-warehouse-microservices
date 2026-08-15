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

    @NotBlank(message = "name é obrigatório")
    private String name;

    @NotBlank(message = "description é obrigatório")
    private String description;

    @NotBlank(message = "documentUrl é obrigatório")
    private String documentUrl;

    @NotNull(message = "epiCategoryId é obrigatório")
    private Long epiCategoryId;

    @NotNull(message = "currentStock é obrigatório")
    @Min(value = 0, message = "currentStock não pode ser negativo")
    private Integer currentStock;
}
