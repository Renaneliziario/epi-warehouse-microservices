package br.com.renan.almoxarifado.epiwithdrawal;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EpiRepository extends JpaRepository<Epi, Long> {

    @Query(value = "SELECT * FROM epi ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Epi> findRandom();
}