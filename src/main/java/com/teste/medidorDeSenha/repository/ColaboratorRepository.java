package com.teste.medidorDeSenha.repository;

import com.teste.medidorDeSenha.domain.Colaborator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboratorRepository extends MongoRepository<Colaborator, String> {
}
