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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ColaboratorService {

    public static final String IS_SUBORDINATE = "SUBORDINATE";
    @Autowired
    private ColaboratorRepository repository;

    @Autowired
    private CredentialService credentialService;

    public ResponseEntity<List<ColaboratorDTO>> findAll() {
        List<Colaborator> colaborators = repository.findAll();
        List<ColaboratorDTO> dtos = colaborators
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());

        dtos = processColaborators(dtos);
        removeRepeatsObjects(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    private void removeRepeatsObjects(List<ColaboratorDTO> dtos) {
        List<ColaboratorDTO> toRemove = new ArrayList<>();


        final int dtosSize = dtos.size();
        List<ColaboratorDTO> finalDtos = dtos;
        dtos.forEach(dto ->{
            for (int i = 0; i < dtosSize; i++) {
                finalDtos.get(i).getSubordinates().forEach(subordinate -> {
                    if(Objects.equals(subordinate.getId(), dto.getId())){
                        toRemove.add(dto);
                    }
                });
            }
        });

        dtos.removeAll(toRemove);
    }

    public List<ColaboratorDTO> processColaborators(List<ColaboratorDTO> dtos) {
        List<ColaboratorDTO> auxDtos = new ArrayList<>(dtos);
        return processSubordinates(auxDtos, auxDtos);
    }

    private List<ColaboratorDTO> processSubordinates(List<ColaboratorDTO> rootList, List<ColaboratorDTO> subordinatesList) {
        List<ColaboratorDTO> result = new ArrayList<>();

        for (ColaboratorDTO dto : subordinatesList) {
            List<ColaboratorDTO> updatedSubordinates = dto.getSubordinates().stream()
                    .map(subordinate -> {
                        Optional<ColaboratorDTO> matchingDto = rootList.stream()
                                .filter(candidate -> Objects.equals(candidate.getId(), subordinate.getId()))
                                .findFirst();

                        return matchingDto.orElse(subordinate);
                    })
                    .collect(Collectors.toList());

            dto.setSubordinates(updatedSubordinates);

            // Chama recursivamente para processar os subordinados da subárvore
            List<ColaboratorDTO> processedSubordinates = processSubordinates(rootList, dto.getSubordinates());

            // Se o próprio DTO ainda existir, adiciona à lista de resultados
            if (rootList.contains(dto)) {
                result.add(dto);
            } else {
                // Se o DTO não estiver mais na lista principal, verifica se algum subordinado ainda existe
                if (!processedSubordinates.isEmpty()) {
                    result.add(dto);
                }
            }
        }

        return result;
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

    public ResponseEntity<ColaboratorDTO> save(ColaboratorDTO colaborator) {
        try {
            Colaborator entity = mapToEntity(colaborator);
            Colaborator savedColaborator = repository.save(entity);
            return new ResponseEntity<>(colaborator, HttpStatus.CREATED);
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

    public ResponseEntity<ColaboratorDTO> update(String id, ColaboratorDTO dto) {
        Optional<Colaborator> existingColaboratorOptional = Objects.equals(id, IS_SUBORDINATE)
                ? repository.findById(dto.getLeadId())
                : repository.findById(id);

        if (existingColaboratorOptional.isPresent()) {
            Colaborator existingColaborator = existingColaboratorOptional.get();
            if (!Objects.equals(id, IS_SUBORDINATE)) {

                if (Objects.nonNull(dto.getPassword())) {
                    Credential oldCredential = existingColaborator.getCredential();
                    Credential newCredential = credentialService.getCredential(dto.getPassword());
                    CredentialHistory history = CredentialHistory
                            .builder()
                            .score(oldCredential.getScore())
                            .password(oldCredential.getPassword())
                            .forcePass(oldCredential.getForcePass())
                            .level(oldCredential.getLevel())
                            .build();

                    if (Objects.nonNull(existingColaborator.getCredentialsHistory())) {
                        existingColaborator.getCredentialsHistory().add(history);
                    } else {
                        existingColaborator.setCredentialsHistory(new HashSet<>());
                        existingColaborator.getCredentialsHistory().add(history);
                    }
                    existingColaborator.setCredential(newCredential);
                } else{
                    existingColaborator.removeSubordinateById(dto.getSubordinateRemoved());
                }
            } else {
                Set<Colaborator> subordinates = Objects.nonNull(existingColaborator.getSubordinates())
                        ? existingColaborator.getSubordinates()
                        : new HashSet<>();

                subordinates.add(saveSubordinate(dto));
                existingColaborator.setSubordinates(subordinates);
            }

            Colaborator updatedColaborator = repository.save(existingColaborator);
            ColaboratorDTO newDto = mapToDTO(updatedColaborator);
            return ResponseEntity.ok(newDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Colaborator saveSubordinate(ColaboratorDTO dto) {
        dto.setId(null);
        Colaborator entity = mapToEntity(dto);
        return repository.save(entity);
    }

    private ColaboratorDTO mapToDTO(Colaborator colaborator) {

        return ColaboratorDTO
                .builder()
                .id(colaborator.getId())
                .name(colaborator.getName())
                .forcePass(colaborator.getCredential().getForcePass())
                .score(colaborator.getCredential().getScore())
                .subordinates(
                        Objects.nonNull(colaborator.getSubordinates())
                                ? colaborator.getSubordinates()
                                .stream().map(this::mapToDTO)
                                .collect(Collectors.toList())
                                : new ArrayList<>())
                .levelPass(colaborator.getCredential().getLevel())
                .build();
    }

    private Colaborator mapToEntity(ColaboratorDTO dto) {
        return Colaborator
                .builder()
                .name(dto.getName())
                .credential(credentialService.getCredential(dto.getPassword()))
                .subordinates(
                        Objects.nonNull(dto.getSubordinates()) ?
                                new HashSet<>(dto.getSubordinates()
                                        .stream().map(this::mapToEntity)
                                        .collect(Collectors.toList()))
                                : new HashSet<>()
                )
                .build();
    }

}
