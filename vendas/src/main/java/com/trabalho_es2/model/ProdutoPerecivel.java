package com.trabalho_es2.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("perecivel")
public class ProdutoPerecivel extends Produto {
    private LocalDate dataValidade;

    public LocalDate getDataValidade() {
        return dataValidade;
    }
    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }
}
