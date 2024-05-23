package com.unifacisa.tcc.colaborador;

import com.unifacisa.tcc.Repository;
import com.unifacisa.tcc.notificacao.EmailNotificacao;
import com.unifacisa.tcc.notificacao.Notificacao;
import com.unifacisa.tcc.notificacao.SMSNotificacao;
import com.unifacisa.tcc.notificacao.TIPO_NOTIFICACAO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.springframework.util.ObjectUtils.isEmpty;


@Data
public class Funcionario {

    private Integer matricula;
    private CARGO_FUNCIONARIO cargoFuncionario;
    private BigDecimal bonus;
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private String nome;
    private String email;
    private String telefone;

    public Funcionario() {
    }

    public Funcionario(int matricula, CARGO_FUNCIONARIO cargoFuncionario, String nome, String email,
                       String telefone, BigDecimal bonus, BigDecimal salario, LocalDate dataAdmissao) {
        this.matricula = matricula;
        this.cargoFuncionario = cargoFuncionario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.bonus = bonus;
        this.salario = salario;
        this.dataAdmissao = dataAdmissao;
    }

    public Funcionario adicionarColaborador(Funcionario funcionario){
        try {
            Repository repository = new Repository();
            return repository.inserirFuncionario(funcionario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir colaborador");
        }
    }

    public void notificarColaborador(Funcionario funcionario, Notificacao notificacao,
                                     TIPO_NOTIFICACAO tipoNotificacao) {

        if(TIPO_NOTIFICACAO.SMS == tipoNotificacao){

            SMSNotificacao smsNotificacao = new SMSNotificacao();
            smsNotificacao.notificarFuncionario(funcionario, notificacao);

        }else if (TIPO_NOTIFICACAO.EMAIL == tipoNotificacao){

            EmailNotificacao emailNotificacao = new EmailNotificacao();
            emailNotificacao.notificarFuncionario(funcionario, notificacao);

        }
    }

    public BigDecimal retornaSalarioBase(CARGO_FUNCIONARIO cargoFuncionario) {

        if (cargoFuncionario == CARGO_FUNCIONARIO.GERENTE) {
            this.salario = BigDecimal.valueOf(7000);

        } else if (cargoFuncionario == CARGO_FUNCIONARIO.VENDEDOR) {
            this.salario = BigDecimal.valueOf(2000);
        } else if (cargoFuncionario == CARGO_FUNCIONARIO.SEGURANCA) {
            this.salario = BigDecimal.valueOf(1800);
        }

        return salario;
    }

    public BigDecimal calcularBonusAnual(LocalDate dataInicio,
                                         CARGO_FUNCIONARIO cargoFuncionario, BigDecimal salario) {
        LocalDate dataAtual = LocalDate.now();
        Long anosTrabalhados = ChronoUnit.YEARS.between(dataInicio, dataAtual);
        BigDecimal bonus = BigDecimal.ZERO;

        if(cargoFuncionario == CARGO_FUNCIONARIO.VENDEDOR){
            if (anosTrabalhados >= 5 && anosTrabalhados < 10) {
                bonus = salario.multiply(BigDecimal.valueOf(0.12));
            }
            else if(anosTrabalhados >= 10){
                bonus = salario.multiply(BigDecimal.valueOf(0.15));
            }
        }else if(cargoFuncionario == CARGO_FUNCIONARIO.GERENTE){
            if (anosTrabalhados >= 5 && anosTrabalhados < 10) {
                bonus = salario.multiply(BigDecimal.valueOf(0.2));
            }
            else if(anosTrabalhados >= 10){
                bonus = salario.multiply(BigDecimal.valueOf(0.25));
            }
        }else if(cargoFuncionario == CARGO_FUNCIONARIO.SEGURANCA){
            if (anosTrabalhados >= 5 && anosTrabalhados < 10) {
                bonus = salario.multiply(BigDecimal.valueOf(0.1));
            }
            else if(anosTrabalhados >= 10){
                bonus = salario.multiply(BigDecimal.valueOf(0.13));
            }
        }
        return bonus;
    }



    public String gerarRelatorioFuncionario(Funcionario funcionario) {
        String relatorio = "Não existe esse colaborador";
        if(!isEmpty(funcionario)){
            relatorio = "Funcionario(a) de matrícula " + funcionario.matricula + "possui salario " + funcionario.salario + " e teve bonus de " + funcionario.bonus +
                    " no mes atual";
        }
        return relatorio;
    }

    public Funcionario dadosColaborador(Integer matricula) throws SQLException {
        Repository repository = new Repository();
        return repository.obterFuncionarioPorId(matricula).
                orElseThrow(() -> new RuntimeException("Funcionario não encontrado por matrícula"));
    }

    public void atualizarDadosColaborador(Funcionario funcionario) {
        try {
            Repository repository = new Repository();
            repository.atualizarFuncionario(funcionario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar");
        }
    }
}
