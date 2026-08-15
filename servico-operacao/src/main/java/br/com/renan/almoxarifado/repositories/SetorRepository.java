package br.com.renan.almoxarifado.repositories;

import br.com.renan.almoxarifado.entities.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetorRepository extends JpaRepository<Setor, Long> {
}
