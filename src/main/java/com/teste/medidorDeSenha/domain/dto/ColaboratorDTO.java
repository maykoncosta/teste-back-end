package com.teste.medidorDeSenha.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ColaboratorDTO {

    private String name;

    private String password;

    private List<ColaboratorDTO> subordinates;

    private int score;

    private int passwordForce;

}
