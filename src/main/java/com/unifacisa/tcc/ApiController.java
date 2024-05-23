package com.unifacisa.tcc;

import com.unifacisa.tcc.veiculos.*;
import com.unifacisa.tcc.cliente.Cliente;
import com.unifacisa.tcc.colaborador.Funcionario;
import com.unifacisa.tcc.notificacao.Notificacao;
import com.unifacisa.tcc.notificacao.TIPO_NOTIFICACAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping(value = "financeiro/colaborador/buscar/{matricula}")
    public ResponseEntity<String> gerarRelatorioColaborador(@PathVariable Integer matricula) throws SQLException {

        Funcionario funcionario = new Funcionario();
        var dadosColaborador = funcionario.dadosColaborador(matricula);

        String relatorio = funcionario.gerarRelatorioFuncionario(dadosColaborador);

        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }

    @GetMapping(value = "financeiro/colaborador/buscar/{matricula}/salario")
    public ResponseEntity<BigDecimal> retornarSalario(@PathVariable Integer matricula) throws SQLException {

        Funcionario funcionario = new Funcionario();
        var dadosColaborador = funcionario.dadosColaborador(matricula);

        BigDecimal salario = funcionario.retornaSalarioBase(dadosColaborador.getCargoFuncionario());

        return new ResponseEntity<>(salario, HttpStatus.OK);
    }

    @GetMapping(value = "financeiro/colaborador/buscar/{matricula}/bonus")
    public ResponseEntity<BigDecimal> calcularBonus(@PathVariable Integer matricula) throws SQLException {

        Funcionario funcionario = new Funcionario();
        var dadosColaborador = funcionario.dadosColaborador(matricula);

        BigDecimal bonus = funcionario.calcularBonusAnual(dadosColaborador.getDataAdmissao(), funcionario.getCargoFuncionario(), funcionario.getSalario());

        return new ResponseEntity<>(bonus, HttpStatus.OK);
    }

    @PostMapping(path = "notificacao/colaborador/{matricula}/notificar")
    public ResponseEntity<HttpStatus> notificarColaborador(@RequestBody Notificacao notificacao, @PathVariable Integer matricula, TIPO_NOTIFICACAO tipoNotificacao) throws SQLException {

        Funcionario funcionario = new Funcionario();
        var dadosColaborador = funcionario.dadosColaborador(matricula);

        if(tipoNotificacao == TIPO_NOTIFICACAO.SMS){
            funcionario.notificarColaborador(dadosColaborador, notificacao, tipoNotificacao);
        }else{
            funcionario.notificarColaborador(dadosColaborador, notificacao, tipoNotificacao);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "colaborador/adicionar")
    public ResponseEntity<Funcionario> adicionarColaborador(@RequestBody Funcionario funcionarioNovo){

        Funcionario funcionario = new Funcionario();
        var colab = funcionario.adicionarColaborador(funcionarioNovo);

        return new ResponseEntity<>(colab, HttpStatus.CREATED);
    }

    @PostMapping(path = "cliente/adicionar")
    public ResponseEntity<Cliente> adicionarCliente(@RequestBody Cliente clienteNovo){

        Cliente cliente = new Cliente();
        var client = cliente.adicionarCliente(clienteNovo);

        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @GetMapping(path = "financeiro/cliente/{id}/desconto")
    public ResponseEntity<BigDecimal> calcularDesconto(@RequestBody Cliente clienteNovo){

        Cliente cliente = new Cliente();
        var desconto = cliente.calcularDesconto(clienteNovo);

        return new ResponseEntity<>(desconto, HttpStatus.OK);
    }

    @PostMapping(path = "notificacao/cliente/{id}/notificar")
    public ResponseEntity<HttpStatus> notificarCliente(@RequestBody Notificacao notificacao, @PathVariable Integer id,
                                                       TIPO_NOTIFICACAO tipoNotificacao) throws SQLException {

        Cliente cliente = new Cliente();
        var dadosCliente = cliente.dadosCliente(id);

        if(tipoNotificacao == TIPO_NOTIFICACAO.SMS){
            cliente.notificarCliente(dadosCliente, notificacao, tipoNotificacao);
        }else{
            cliente.notificarCliente(dadosCliente, notificacao, tipoNotificacao);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "veiculo/carro/adicionar")
    public ResponseEntity<Carro> adicionarCarro(@RequestBody Carro carroNovo) throws SQLException {

        Repository repository = new Repository();
        Carro carro = repository.inserirCarro(carroNovo);

        return new ResponseEntity<>(carro, HttpStatus.CREATED);
    }

    @PostMapping(path = "cliente/editar")
    public ResponseEntity<HttpStatus> alterarDadosCliente(@RequestBody Cliente cliente){

        Cliente cli = new Cliente();
        cli.atualizarDadosCliente(cliente);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "veiculo/carro")
    public ResponseEntity<Carro> getCarro(@RequestBody Carro carroRequest) throws SQLException {

        Repository repository = new Repository();
        Carro carro = repository.obterCarroPorId(carroRequest.getId()).get();

        return new ResponseEntity<>(carro, HttpStatus.OK);
    }

    @GetMapping(path = "veiculo/carroEletrico")
    public ResponseEntity<CarroEletrico> getCarroEletrico(@RequestBody CarroEletrico carroEletricoRequest) throws SQLException {

        Repository repository = new Repository();
        CarroEletrico carroEletrico = repository.obterCarroEletricoPorId(carroEletricoRequest.getId()).get();

        return new ResponseEntity<>(carroEletrico, HttpStatus.OK);
    }

    @PostMapping(path = "veiculo/carroEletrico/adicionar")
    public ResponseEntity<CarroEletrico> adicionarCarroEletrico(@RequestBody CarroEletrico carroEletricoNovo) throws SQLException {

        Repository repository = new Repository();
        CarroEletrico carroEletrico = repository.inserirCarroEletrico(carroEletricoNovo);

        return new ResponseEntity<>(carroEletrico, HttpStatus.CREATED);
    }

    @GetMapping(path = "veiculo/moto")
    public ResponseEntity<Moto> getMoto(@RequestBody Moto motoRequest) throws SQLException {

        Repository repository = new Repository();
        Moto moto = repository.obterMotoPorId(motoRequest.getId()).get();

        return new ResponseEntity<>(moto, HttpStatus.OK);
    }

    @PostMapping(path = "veiculo/moto/adicionar")
    public ResponseEntity<Moto> adicionarMoto(@RequestBody Moto motoNova) throws SQLException {

        Repository repository = new Repository();
        Moto moto = repository.inserirMoto(motoNova);

        return new ResponseEntity<>(moto, HttpStatus.CREATED);
    }

    @GetMapping(path = "veiculo/valorTanqueCheio")
    public ResponseEntity<BigDecimal> getValorTanqueCheio(@RequestBody Veiculo veiculo, BigDecimal valorCombustivelDia) throws SQLException {

        BigDecimal valor = veiculo.getValorTanqueCheio(valorCombustivelDia);

        return new ResponseEntity<>(valor, HttpStatus.OK);
    }

}
