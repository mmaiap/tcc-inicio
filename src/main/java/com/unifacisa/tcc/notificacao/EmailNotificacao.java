package com.unifacisa.tcc.notificacao;

import com.unifacisa.tcc.cliente.Cliente;
import com.unifacisa.tcc.colaborador.Funcionario;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailNotificacao implements NotificacaoService {

    @Value("${account.email}")
    private String remetente;
    @Value("${account.password}")
    private String senha;
    private Properties props;

    public EmailNotificacao() {
        // Configurações do servidor SMTP do Gmail
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp@gmail.com");
        props.put("mail.smtp.port", "587");
    }

    private void enviarEmail(String destinatario, String assunto, String corpo) {
        // Autenticação
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha);
            }
        });

        try {
            // Criação da mensagem
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);

            // Envio do email
            Transport.send(message);

            System.out.println("Email enviado com sucesso para: " + destinatario);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notificarCliente(Cliente cliente, Notificacao notificacao) {
        this.enviarEmail(cliente.getTelefone(), notificacao.getAssunto(), notificacao.getTexto());
    }

    @Override
    public void notificarFuncionario(Funcionario funcionario, Notificacao notificacao) {
        this.enviarEmail(funcionario.getTelefone(), notificacao.getAssunto(), notificacao.getTexto());
    }
}

