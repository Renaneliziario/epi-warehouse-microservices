package br.com.renan.almoxarifado.repositories;

import br.com.renan.almoxarifado.entities.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperacaoRepository extends JpaRepository<Operacao, Long> {
}
