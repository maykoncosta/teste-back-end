package com.teste.medidorDeSenha.config;


import com.teste.medidorDeSenha.domain.Audit;

import java.util.Date;

public class AuditListener {

    private void antesDeSalvar(Audit audit){
        audit.setDataCriacao(new Date());
        audit.setDataUltimaAtualizacao(new Date());
        audit.setUsuarioAtualizacao("dev");
        audit.setUsuarioCriacao("dev");
    }

    private void antesDeAtualizar(Audit audit){
        audit.setDataUltimaAtualizacao(new Date());
        audit.setUsuarioAtualizacao("dev");
    }

}
