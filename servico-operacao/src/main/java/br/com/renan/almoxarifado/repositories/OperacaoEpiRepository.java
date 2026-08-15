package br.com.renan.almoxarifado.repositories;

import br.com.renan.almoxarifado.entities.OperacaoEpi;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperacaoEpiRepository extends JpaRepository<OperacaoEpi, Long> {

    List<OperacaoEpi> findByOperacaoId(long operacaoId);
}
