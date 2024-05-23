package com.unifacisa.tcc.veiculos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class Veiculo implements VeiculoService{

    public Integer id;
    public String nome;
    public String modelo;
    public Integer ano;
    public BigDecimal preco;
    public BigDecimal capacidadeTanque;
}
