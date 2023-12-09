package com.teste.medidorDeSenha.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Data
@MappedSuperclass
public class Audit {

    @NotNull
    private Date dataCriacao;

    private Date dataUltimaAtualizacao;

    @NotNull
    private String usuarioCriacao;

    @NotNull
    private String usuarioAtualizacao;
}
