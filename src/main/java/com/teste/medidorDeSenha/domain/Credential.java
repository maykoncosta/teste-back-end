package com.teste.medidorDeSenha.domain;

import com.teste.medidorDeSenha.config.AuditListener;
import jakarta.persistence.EntityListeners;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
@EntityListeners(AuditListener.class)
public class Credential extends Audit{

    @Id
    private String id;

    private int score;

    private String password;

    private int passwordStrength;
}
