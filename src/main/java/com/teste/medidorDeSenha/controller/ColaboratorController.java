package com.teste.medidorDeSenha.controller;

import com.teste.medidorDeSenha.domain.Colaborator;
import com.teste.medidorDeSenha.domain.dto.ColaboratorDTO;
import com.teste.medidorDeSenha.service.ColaboratorService;
import com.teste.medidorDeSenha.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaborators")
public class ColaboratorController {

    @Autowired
    private ColaboratorService service;

    @Autowired
    private CredentialService credentialService;

    @GetMapping
    public ResponseEntity<List<ColaboratorDTO>> getAllColaborators() {
        List<ColaboratorDTO> colaborators = service.findAll().getBody();
        return new ResponseEntity<>(colaborators, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboratorDTO> getColaboratorById(@PathVariable String id) {
        ResponseEntity<ColaboratorDTO> response = service.findById(id);
        return response != null ? response : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ColaboratorDTO> createColaborator(@RequestBody ColaboratorDTO colaborator) {
        ResponseEntity<ColaboratorDTO> response = service.save(colaborator);
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboratorDTO> updateColaborator(@PathVariable String id, @RequestBody Colaborator colaborator) {
        ResponseEntity<ColaboratorDTO> response = service.update(id, colaborator);
        return response != null ? response : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColaborator(@PathVariable String id) {
        ResponseEntity<Void> response = service.delete(id);
        return response != null ? response : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/generate-pass", produces = "text/plain")
    public ResponseEntity<String> getGeneratePassword() {
        String passwordGenerated = credentialService.generatePassword();
        return new ResponseEntity<>(passwordGenerated, HttpStatus.CREATED);
    }
}
