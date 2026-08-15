package br.com.renan.almoxarifado.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "operacao_risco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoRisco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long operacaoId;

    @Column(nullable = false)
    private long riscoId;

    @Column(nullable = false)
    private String nivelRisco;

    private String observacao;
}
