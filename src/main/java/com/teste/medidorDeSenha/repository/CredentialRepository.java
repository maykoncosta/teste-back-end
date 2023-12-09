package com.teste.medidorDeSenha.repository;

import com.teste.medidorDeSenha.domain.Credential;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CredentialRepository extends MongoRepository<Credential, String> {
}
