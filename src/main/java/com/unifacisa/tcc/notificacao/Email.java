package com.unifacisa.tcc.notificacao;

import lombok.Data;

@Data
public class Email extends Notificacao{

    private String assunto;
    private String destinatario;

}
