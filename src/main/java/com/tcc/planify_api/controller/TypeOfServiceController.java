package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.TypeOfServiceApi;
import com.tcc.planify_api.dto.typeOfService.TypeOfServiceCreateDTO;
import com.tcc.planify_api.dto.typeOfService.TypeOfServiceDTO;
import com.tcc.planify_api.service.TypeOfServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

  @RestController
  @RequiredArgsConstructor
  public class TypeOfServiceController implements TypeOfServiceApi {

    private final TypeOfServicesService service;

    @Override
    public ResponseEntity<List<TypeOfServiceDTO>> getAllServices() {
      List<TypeOfServiceDTO> services = service.getAllServices();
      return ResponseEntity.ok(services);
    }

    @Override
    public ResponseEntity<TypeOfServiceDTO> getServiceById(Long id) {
      TypeOfServiceDTO serviceDTO = service.getServiceById(id);
      return ResponseEntity.ok(serviceDTO);
    }

    @Override
    public ResponseEntity<TypeOfServiceDTO> createService(TypeOfServiceCreateDTO serviceCreateDTO) {
      TypeOfServiceDTO created = service.createService(serviceCreateDTO);
      return ResponseEntity.status(201).body(created);
    }

    @Override
    public ResponseEntity<TypeOfServiceDTO> updateService(Long id, TypeOfServiceCreateDTO serviceUpdateDTO) {
      TypeOfServiceDTO updated = service.updateService(id, serviceUpdateDTO);
      return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteService(Long id) {
      service.deleteService(id);
      return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TypeOfServiceDTO>> searchByName(String name) {
      List<TypeOfServiceDTO> services = service.searchByName(name);
      return ResponseEntity.ok(services);
    }
}
