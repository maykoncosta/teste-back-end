package com.teste.medidorDeSenha.repository;

import com.teste.medidorDeSenha.domain.CredentialHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CredentialHistoryRepository extends MongoRepository<CredentialHistory, String> {
}
