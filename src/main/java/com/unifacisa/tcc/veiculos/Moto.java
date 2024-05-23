package com.unifacisa.tcc.veiculos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Moto extends Veiculo {

    public Integer id;
    public String nome;
    public String modelo;
    public Integer ano;
    public BigDecimal preco;

    public Moto(Integer id, String nome, String modelo, Integer ano, BigDecimal preco, BigDecimal capacidadeTanque) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
        this.capacidadeTanque = capacidadeTanque;
    }


    @Override
    public BigDecimal getValorTanqueCheio(BigDecimal valorCombustivelDia) {
        return valorCombustivelDia.multiply(this.capacidadeTanque);
    }
}
