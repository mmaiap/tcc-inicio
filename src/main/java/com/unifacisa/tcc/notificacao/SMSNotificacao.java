package com.unifacisa.tcc.notificacao;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.unifacisa.tcc.cliente.Cliente;
import com.unifacisa.tcc.colaborador.Funcionario;
import org.springframework.beans.factory.annotation.Value;

public class SMSNotificacao implements NotificacaoService {

    @Value("${twilio.id-conta}")
    private String conta;
    @Value("${twilio.token}")
    private String token;

    private static String NUMERO_EMPRESA = "83998980000";

    public SMSNotificacao() {
        Twilio.init(this.conta, this.token);
    }

    private void enviarSMS(String numero, String mensagem) {
        com.twilio.rest.api.v2010.account.Message.creator(
                new PhoneNumber(numero),
                new PhoneNumber(NUMERO_EMPRESA),
                mensagem
        ).create();
    }

    @Override
    public void notificarCliente(Cliente cliente, Notificacao notificacao) {
        this.enviarSMS(cliente.getTelefone(), notificacao.getTexto());
    }

    @Override
    public void notificarFuncionario(Funcionario funcionario, Notificacao notificacao) {
        this.enviarSMS(funcionario.getTelefone(), notificacao.getTexto());
    }
}
