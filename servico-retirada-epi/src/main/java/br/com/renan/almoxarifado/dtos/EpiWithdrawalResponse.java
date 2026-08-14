package br.com.renan.almoxarifado.dtos;

import br.com.renan.almoxarifado.entities.EpiWithdrawal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpiWithdrawalResponse {

    private long id;
    private long epiId;
    private long employeeId;
    private int quantity;
    private LocalDateTime withdrawnAt;

    public static EpiWithdrawalResponse from(EpiWithdrawal withdrawal) {
        return new EpiWithdrawalResponse(
                withdrawal.getId(),
                withdrawal.getEpiId(),
                withdrawal.getEmployeeId(),
                withdrawal.getQuantity(),
                withdrawal.getWithdrawnAt()
        );
    }
}
