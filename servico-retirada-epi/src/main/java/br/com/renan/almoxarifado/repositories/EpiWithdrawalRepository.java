package br.com.renan.almoxarifado.repositories;

import br.com.renan.almoxarifado.entities.EpiWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpiWithdrawalRepository extends JpaRepository<EpiWithdrawal, Long> {
}
