package com.teste.medidorDeSenha.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "credential")
@Builder
public class Credential extends Audit{

    @Id
    private String id;

    private int score;

    private byte[] password;

    private String forcePass;

    private int level;
}
