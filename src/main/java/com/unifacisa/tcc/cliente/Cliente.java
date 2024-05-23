package com.unifacisa.tcc.cliente;

import com.unifacisa.tcc.Repository;
import com.unifacisa.tcc.notificacao.EmailNotificacao;
import com.unifacisa.tcc.notificacao.Notificacao;
import com.unifacisa.tcc.notificacao.SMSNotificacao;
import com.unifacisa.tcc.notificacao.TIPO_NOTIFICACAO;
import com.unifacisa.tcc.veiculos.Veiculo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.SQLException;


@Data
public class Cliente {

    private Integer id;
    private Double saldoEmCompras;
    private Veiculo veiculo;
    private String nome;
    private String email;
    private String telefone;

    public Cliente(Integer id, Double saldoEmCompras, Veiculo veiculo) {
        this.id = id;
        this.saldoEmCompras = saldoEmCompras;
        this.veiculo = veiculo;
    }

    public Cliente() {
    }

    public Cliente(int id, String nome, String email, String telefone, Double saldoEmCompras) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.saldoEmCompras = saldoEmCompras;
    }

    public void notificarCliente(Cliente cliente, Notificacao notificacao,
                                 TIPO_NOTIFICACAO tipoNotificacao) {

        if(TIPO_NOTIFICACAO.SMS == tipoNotificacao){

            SMSNotificacao smsNotificacao = new SMSNotificacao();
            smsNotificacao.notificarCliente(cliente, notificacao);

        }else if (TIPO_NOTIFICACAO.EMAIL == tipoNotificacao){

            EmailNotificacao emailNotificacao = new EmailNotificacao();
            emailNotificacao.notificarCliente(cliente, notificacao);

        }
    }

    public Cliente adicionarCliente(Cliente cliente){
        try {
            Repository repository = new Repository();
            return repository.inserirCliente(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir cliente");
        }
    }

    public Cliente dadosCliente(Integer id) throws SQLException {
        Repository repository = new Repository();
        return repository.obterClientePorId(id).
                orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public void atualizarDadosCliente(Cliente cliente) {
        try {
            Repository repository = new Repository();
            repository.atualizarCliente(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar");
        }
    }


    public BigDecimal calcularDesconto(Cliente cliente) {

        BigDecimal desconto = BigDecimal.valueOf(0);

        if (cliente.getSaldoEmCompras() >= 500) {
            desconto = BigDecimal.valueOf(0.1);
        }else if(cliente.getSaldoEmCompras() <= 5000 && cliente.getSaldoEmCompras() >= 2000){
            desconto = BigDecimal.valueOf(0.2);
        }

        return desconto;
    }


}
