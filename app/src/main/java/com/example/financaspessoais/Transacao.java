package com.example.financaspessoais;

import java.io.Serializable;

public class Transacao implements Serializable {
    private int id;
    private String descricao;
    private double valor;
    private String tipo; // "RECEITA" ou "DESPESA"
    private String categoria;
    private String data;

    public Transacao() {}

    public Transacao(String descricao, double valor, String tipo, String categoria, String data) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
    }

    public Transacao(int id, String descricao, double valor, String tipo, String categoria, String data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
    }

    // Getters
    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public String getTipo() { return tipo; }
    public String getCategoria() { return categoria; }
    public String getData() { return data; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(double valor) { this.valor = valor; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setData(String data) { this.data = data; }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", tipo='" + tipo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}