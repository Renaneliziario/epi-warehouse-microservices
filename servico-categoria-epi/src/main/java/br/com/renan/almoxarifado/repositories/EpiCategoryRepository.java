package br.com.renan.almoxarifado.repositories;

import br.com.renan.almoxarifado.entities.EpiCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpiCategoryRepository extends JpaRepository<EpiCategory, Long> {
}
