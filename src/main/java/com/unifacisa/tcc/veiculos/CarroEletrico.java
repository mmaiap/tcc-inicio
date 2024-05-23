package com.unifacisa.tcc.veiculos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarroEletrico extends Veiculo {

    public Integer id;
    public String nome;
    public String modelo;
    public Integer ano;
    public BigDecimal preco;

    public CarroEletrico(Integer id, String nome, String modelo, Integer ano, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
    }

    @Override
    public BigDecimal getValorTanqueCheio(BigDecimal valorCombustivelDia) {
        return BigDecimal.ZERO;
    }
}
