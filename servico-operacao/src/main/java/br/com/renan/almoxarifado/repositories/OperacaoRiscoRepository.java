package br.com.renan.almoxarifado.repositories;

import br.com.renan.almoxarifado.entities.OperacaoRisco;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperacaoRiscoRepository extends JpaRepository<OperacaoRisco, Long> {

    List<OperacaoRisco> findByOperacaoId(long operacaoId);
}
