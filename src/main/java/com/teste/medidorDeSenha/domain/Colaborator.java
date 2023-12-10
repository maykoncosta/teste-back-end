package com.teste.medidorDeSenha.domain;

import com.teste.medidorDeSenha.config.AuditListener;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Document(collection = "colaborators")
@Data
@Builder
public class Colaborator extends Audit{

    @Id
    private String id;

    private String name;

    private Set<Colaborator> subordinates;

    private Credential credential;

    private Set<CredentialHistory> credentialsHistory;

    public boolean removeSubordinateById(String subordinateId) {
        // Tenta remover o subordinado diretamente da lista
        if (subordinates.removeIf(subordinate -> Objects.equals(subordinate.getId(), subordinateId))) {
            return true;
        }

        // Se o subordinado não foi removido diretamente, tenta remover recursivamente dos subordinados
        for (Colaborator subordinate : subordinates) {
            if (subordinate.removeSubordinateById(subordinateId)) {
                // Se o subordinado foi removido recursivamente e sua lista de subordinados ficou vazia, remove o subordinado da lista principal
                if (subordinate.getSubordinates().isEmpty()) {
                    subordinates.remove(subordinate);
                }
                return true;
            }
        }

        return false;
    }

}
