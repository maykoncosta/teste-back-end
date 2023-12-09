package com.teste.medidorDeSenha.service;

import com.teste.medidorDeSenha.domain.Colaborator;
import com.teste.medidorDeSenha.domain.Credential;
import com.teste.medidorDeSenha.domain.CredentialHistory;
import com.teste.medidorDeSenha.domain.dto.ColaboratorDTO;
import com.teste.medidorDeSenha.repository.ColaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColaboratorService {

    @Autowired
    private ColaboratorRepository repository;

    @Autowired
    private CredentialService credentialService;

    public ResponseEntity<List<ColaboratorDTO>> findAll() {
        List<Colaborator> colaborators = repository.findAll();
        List<ColaboratorDTO> dtos = colaborators.stream().map(this::mapToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    public ResponseEntity<ColaboratorDTO> findById(String id) {
        Optional<Colaborator> colaboratorOptional = repository.findById(id);
        if (colaboratorOptional.isPresent()) {
            ColaboratorDTO dto = mapToDTO(colaboratorOptional.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ColaboratorDTO> save(Colaborator colaborator) {
        try {
            Colaborator savedColaborator = repository.save(colaborator);
            ColaboratorDTO dto = mapToDTO(savedColaborator);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            // Tratamento de exceção ao salvar
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> delete(String id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Tratamento de exceção ao excluir
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ColaboratorDTO> update(String id, Colaborator colaborator) {
        Optional<Colaborator> existingColaboratorOptional = repository.findById(id);

        if (existingColaboratorOptional.isPresent()) {
            Colaborator existingColaborator = existingColaboratorOptional.get();
            Credential credential = colaborator.getCredential();
            CredentialHistory history = CredentialHistory
                    .builder()
                    .score(credential.getScore())
                    .password(credential.getPassword())
                    .passwordStrength(credential.getPasswordStrength())
                    .build();
            colaborator.getCredentialsHistory().add(history);

            existingColaborator.setSubordinates(colaborator.getSubordinates());
            existingColaborator.setCredential(colaborator.getCredential());
            existingColaborator.setCredentialsHistory(colaborator.getCredentialsHistory());

            Colaborator updatedColaborator = repository.save(existingColaborator);
            ColaboratorDTO dto = mapToDTO(updatedColaborator);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ColaboratorDTO mapToDTO(Colaborator colaborator) {
        ColaboratorDTO dto = new ColaboratorDTO();
        dto.setName(colaborator.getName());
        dto.setSubordinates(colaborator.getSubordinates().stream().map(this::mapToDTO).collect(Collectors.toList()));
        dto.setScore(colaborator.getCredential().getScore());
        dto.setPasswordForce(colaborator.getCredential().getPasswordStrength());
        return dto;
    }
}