package com.teste.medidorDeSenha.domain;

import com.teste.medidorDeSenha.config.AuditListener;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credentials_history")
@Data
@Builder
public class CredentialHistory extends Audit{

    @Id
    private String id;

    private int score;

    private byte[] password;

    private String forcePass;

    private int level;

    private String validityPeriod;

}
