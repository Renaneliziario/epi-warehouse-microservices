package br.com.renan.almoxarifado.epiwithdrawal;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpiResponse {

    private long id;
    private String name;
    private String description;
    private String documentUrl;
    private long epiCategoryId;
    private int currentStock;
    private LocalDateTime registeredAt;

    static EpiResponse from(Epi epi) {
        return new EpiResponse(
                epi.getId(),
                epi.getName(),
                epi.getDescription(),
                epi.getDocumentUrl(),
                epi.getEpiCategoryId(),
                epi.getCurrentStock(),
                epi.getRegisteredAt()
        );
    }
}
