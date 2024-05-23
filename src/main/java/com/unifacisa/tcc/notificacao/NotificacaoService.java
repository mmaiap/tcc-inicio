package com.unifacisa.tcc.notificacao;

import com.unifacisa.tcc.cliente.Cliente;
import com.unifacisa.tcc.colaborador.Funcionario;

public interface NotificacaoService {

    public void notificarCliente(Cliente cliente, Notificacao notificacao);
    public void notificarFuncionario(Funcionario cliente, Notificacao notificacao);

}


