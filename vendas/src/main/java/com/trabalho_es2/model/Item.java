package com.trabalho_es2.model;

import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigoItem;
    
    private int qtde;
    private double valorItem;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Long getCodigoItem() {
        return codigoItem;
    }
    public void setCodigoItem(Long codigoItem) {
        this.codigoItem = codigoItem;
    }

    public int getQtde() {
        return qtde;
    }
    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public double getValorItem() {
        return valorItem;
    }
    public void setValorItem(double valorItem) {
        this.valorItem = valorItem;
    }

    public Pedido getPedido() {
        return pedido;
    }
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}