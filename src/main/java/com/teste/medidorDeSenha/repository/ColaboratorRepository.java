package com.teste.medidorDeSenha.repository;

import com.teste.medidorDeSenha.domain.Colaborator;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColaboratorRepository extends MongoRepository<Colaborator, String> {
}
