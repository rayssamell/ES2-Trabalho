package com.trabalho_es2.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("eletronico")
public class ProdutoEletronico extends Produto {
    private String voltagem;    

    public String getVoltagem() {
        return voltagem;
    }
    public void setVoltagem(String voltagem) {
        this.voltagem = voltagem;
    }
}
