package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.typeOfService.TypeOfServiceCreateDTO;
import com.tcc.planify_api.dto.typeOfService.TypeOfServiceDTO;
import com.tcc.planify_api.entity.TypeOfServiceEntity;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.repository.TypeOfServiceRepository;
import com.tcc.planify_api.repository.UserRepository;
import com.tcc.planify_api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeOfServicesService {

  private final TypeOfServiceRepository repository;
  private final UserRepository userRepository;

  // CREATE
  @Transactional
  public TypeOfServiceDTO createService(TypeOfServiceCreateDTO dto) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    UserEntity owner = userRepository.findById(ownerId)
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

    TypeOfServiceEntity entity = TypeOfServiceEntity.builder()
          .name(dto.getName())
          .description(dto.getDescription())
          .price(dto.getPrice())
          .category(dto.getCategory())
          .duration(dto.getDuration())
          .owner(owner)
          .build();

    return mapToDTO(repository.save(entity));
  }

  // UPDATE
  @Transactional
  public TypeOfServiceDTO updateService(Long id, TypeOfServiceCreateDTO dto) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    TypeOfServiceEntity entity = repository.findByIdAndOwnerId(id, ownerId)
          .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

    if (dto.getName() != null) entity.setName(dto.getName());
    if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
    if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
    if (dto.getCategory() != null) entity.setCategory(dto.getCategory());
    if (dto.getDuration() != null) entity.setDuration(dto.getDuration());

    return mapToDTO(repository.save(entity));
  }

  // GET BY ID
  @Transactional(readOnly = true)
  public TypeOfServiceDTO getServiceById(Long id) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    TypeOfServiceEntity entity = repository.findByIdAndOwnerId(id, ownerId)
          .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

    return mapToDTO(entity);
  }

  // LIST ALL DO USER
  @Transactional(readOnly = true)
  public List<TypeOfServiceDTO> getAllServices() {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    return repository.findByOwnerId(ownerId)
          .stream()
          .map(this::mapToDTO)
          .toList();
  }

  // SEARCH (do user!)
  @Transactional(readOnly = true)
  public List<TypeOfServiceDTO> searchByName(String name) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    return repository.findByOwnerIdAndNameContainingIgnoreCase(ownerId, name)
          .stream()
          .map(this::mapToDTO)
          .toList();
  }

  // DELETE
  @Transactional
  public void deleteService(Long id) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    TypeOfServiceEntity entity = repository.findByIdAndOwnerId(id, ownerId)
          .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

    repository.delete(entity);
  }

  private TypeOfServiceDTO mapToDTO(TypeOfServiceEntity entity) {
    TypeOfServiceDTO dto = new TypeOfServiceDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setPrice(entity.getPrice());
    dto.setCategory(entity.getCategory());
    dto.setDuration(entity.getDuration());
    dto.setOwnerId(entity.getOwner().getId());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }
}