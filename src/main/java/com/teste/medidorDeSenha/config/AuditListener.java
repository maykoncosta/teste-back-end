package com.teste.medidorDeSenha.config;


import com.teste.medidorDeSenha.domain.Audit;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class AuditListener {

    @PrePersist
    private void antesDeSalvar(Audit audit){
        audit.setDataCriacao(new Date());
        audit.setDataUltimaAtualizacao(new Date());
        audit.setUsuarioAtualizacao("dev");
        audit.setUsuarioCriacao("dev");
    }

    @PreUpdate
    private void antesDeAtualizar(Audit audit){
        audit.setDataUltimaAtualizacao(new Date());
        audit.setUsuarioAtualizacao("dev");
    }

}
