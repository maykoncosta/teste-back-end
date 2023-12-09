package com.teste.medidorDeSenha.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Audit {

    private Date dataCriacao;

    private Date dataUltimaAtualizacao;

    private String usuarioCriacao;

    private String usuarioAtualizacao;
}
