package com.teste.medidorDeSenha.domain;

import com.teste.medidorDeSenha.config.AuditListener;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "colaborators")
@Data
public class Colaborator extends Audit{

    @Id
    private String id;

    private String name;

    private Set<Colaborator> subordinates;

    private Credential credential;

    private Set<CredentialHistory> credentialsHistory;

}
