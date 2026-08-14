package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.EpiCategory;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpiCategoryResponse {

    private long id;
    private String name;
    private String description;
    private LocalDateTime registeredAt;

    public static EpiCategoryResponse from(EpiCategory category) {
        return new EpiCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getRegisteredAt()
        );
    }
}
