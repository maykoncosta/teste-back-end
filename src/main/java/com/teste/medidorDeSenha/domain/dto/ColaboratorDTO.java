package com.teste.medidorDeSenha.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ColaboratorDTO {

    private String id;

    private String name;

    private String password;

    private List<ColaboratorDTO> subordinates;

    private int score;

    private String forcePass;

    private int levelPass;

    private String leadId;

    private String subordinateRemoved;

}
