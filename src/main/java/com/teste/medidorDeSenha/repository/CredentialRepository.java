package com.teste.medidorDeSenha.repository;

import com.teste.medidorDeSenha.domain.Credential;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@EnableMongoRepositories
public interface CredentialRepository extends MongoRepository<Credential, String> {
}
