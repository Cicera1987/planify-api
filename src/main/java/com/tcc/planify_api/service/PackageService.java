package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.packageServices.PackageCreateDTO;
import com.tcc.planify_api.dto.packageServices.PackageDTO;
import com.tcc.planify_api.dto.typeOfService.TypeOfServiceDTO;
import com.tcc.planify_api.entity.PackageEntity;
import com.tcc.planify_api.entity.PackageServiceEntity;
import com.tcc.planify_api.entity.TypeOfServiceEntity;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.repository.PackageRepository;
import com.tcc.planify_api.repository.PackageServiceRepository;
import com.tcc.planify_api.repository.TypeOfServiceRepository;
import com.tcc.planify_api.repository.UserRepository;
import com.tcc.planify_api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {

  private final PackageRepository packageRepository;
  private final TypeOfServiceRepository typeOfServiceRepository;
  private final UserRepository userRepository;
  private final PackageServiceRepository packageServiceRepository;

  @Transactional
  public PackageDTO createPackage(PackageCreateDTO dto) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    UserEntity owner = userRepository.findById(ownerId)
          .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

    PackageEntity pkg = PackageEntity.builder()
          .name(dto.getName())
          .totalPrice(dto.getTotalPrice())
          .numberSessions(dto.getNumberSessions())
          .owner(owner)
          .build();

    PackageEntity saved = packageRepository.save(pkg);

    Map<Long, Long> serviceCount = dto.getServiceIds().stream()
          .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

    List<PackageServiceEntity> relations = new ArrayList<>();

    for (var entry : serviceCount.entrySet()) {
      TypeOfServiceEntity service = typeOfServiceRepository
            .findByIdAndOwnerId(entry.getKey(), ownerId)
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado ou não pertence ao usuário."));

      relations.add(PackageServiceEntity.builder()
            .packageEntity(saved)
            .service(service)
            .quantity(entry.getValue().intValue())
            .build());
    }

    packageServiceRepository.saveAll(relations);

    return mapToDTO(saved);
  }

  @Transactional(readOnly = true)
  public List<PackageDTO> listPackagesByOwner() {
    Long ownerId = AuthUtil.getAuthenticatedUserId();
    return packageRepository.findByOwnerId(ownerId)
          .stream()
          .map(this::mapToDTO)
          .toList();
  }

  @Transactional
  public PackageDTO updatePackage(Long id, PackageCreateDTO dto) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    PackageEntity pkg = packageRepository.findByIdAndOwnerId(id, ownerId)
          .orElseThrow(() -> new RuntimeException("Pacote não encontrado ou não pertence ao usuário."));

    pkg.setName(dto.getName());
    pkg.setTotalPrice(dto.getTotalPrice());
    pkg.setNumberSessions(dto.getNumberSessions());

    // Remove serviços antigos (orphanRemoval já faz, mas você limpou o repositório)
    packageServiceRepository.deleteByPackageEntityId(pkg.getId());

    // Monta os novos
    Map<Long, Long> serviceCount = dto.getServiceIds().stream()
          .collect(Collectors.groupingBy(serviceId -> serviceId, Collectors.counting()));

    List<PackageServiceEntity> newRelations = new ArrayList<>();

    for (var entry : serviceCount.entrySet()) {
      TypeOfServiceEntity service = typeOfServiceRepository
            .findByIdAndOwnerId(entry.getKey(), ownerId)
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado ou não pertence ao usuário."));

      newRelations.add(PackageServiceEntity.builder()
            .packageEntity(pkg)
            .service(service)
            .quantity(entry.getValue().intValue())
            .build());
    }

    packageServiceRepository.saveAll(newRelations);

    return mapToDTO(pkg);
  }

  @Transactional
  public void deletePackage(Long id) {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    PackageEntity pkg = packageRepository.findByIdAndOwnerId(id, ownerId)
          .orElseThrow(() -> new RuntimeException("Pacote não encontrado ou não pertence ao usuário."));

    packageServiceRepository.deleteByPackageEntityId(pkg.getId());
    packageRepository.delete(pkg);
  }

  private PackageDTO mapToDTO(PackageEntity entity) {
    PackageDTO dto = new PackageDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setTotalPrice(entity.getTotalPrice());
    dto.setNumberSessions(entity.getNumberSessions());
    dto.setOwnerId(entity.getOwner().getId());
    dto.setCreatedAt(entity.getCreatedAt());

    List<PackageServiceEntity> relations = packageServiceRepository.findByPackageEntityId(entity.getId());

    dto.setServices(relations.stream().map(ps -> {
      TypeOfServiceEntity s = ps.getService();

      TypeOfServiceDTO serviceDto = new TypeOfServiceDTO();
      serviceDto.setId(s.getId());
      serviceDto.setName(s.getName());
      serviceDto.setDescription(s.getDescription());
      serviceDto.setPrice(s.getPrice());
      serviceDto.setCategory(s.getCategory());
      serviceDto.setDuration(s.getDuration());
      serviceDto.setOwnerId(s.getOwner().getId());
      serviceDto.setCreatedAt(s.getCreatedAt());
      serviceDto.setQuantity(ps.getQuantity());

      return serviceDto;
    }).toList());

    return dto;
  }
}