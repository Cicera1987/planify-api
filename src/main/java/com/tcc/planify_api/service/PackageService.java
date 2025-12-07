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

import java.time.LocalDateTime;
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
          .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + ownerId));

    PackageEntity packageEntity = new PackageEntity();
    packageEntity.setName(dto.getName());
    packageEntity.setTotalPrice(dto.getTotalPrice());
    packageEntity.setNumberSessions(dto.getNumberSessions());
    packageEntity.setOwner(owner);
    packageEntity.setCreatedAt(LocalDateTime.now());

    PackageEntity savedPackage = packageRepository.save(packageEntity);

    Map<Long, Long> serviceCount = dto.getServiceIds().stream()
          .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

    List<PackageServiceEntity> packageServices = new ArrayList<>();
    for (Map.Entry<Long, Long> entry : serviceCount.entrySet()) {
      TypeOfServiceEntity service = typeOfServiceRepository.findById(entry.getKey())
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado com id: " + entry.getKey()));

      PackageServiceEntity ps = new PackageServiceEntity();
      ps.setPackageEntity(savedPackage);
      ps.setService(service);
      ps.setQuantity(entry.getValue().intValue());

      packageServices.add(ps);
    }

    packageServiceRepository.saveAll(packageServices);

    return mapToDTO(savedPackage);
  }

  @Transactional(readOnly = true)
  public List<PackageDTO> listPackagesByOwner() {
    Long ownerId = AuthUtil.getAuthenticatedUserId();

    List<PackageEntity> packages = packageRepository.findByOwnerId(ownerId);
    List<PackageDTO> dtoList = new ArrayList<>();

    for (PackageEntity pkg : packages) {
      dtoList.add(mapToDTO(pkg));
    }
    return dtoList;
  }

  @Transactional
  public PackageDTO updatePackage(Long id, PackageCreateDTO dto) {
    PackageEntity packageEntity = packageRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Pacote não encontrado"));

    packageEntity.setName(dto.getName());
    packageEntity.setTotalPrice(dto.getTotalPrice());
    packageEntity.setNumberSessions(dto.getNumberSessions());

    List<PackageServiceEntity> oldServices = packageServiceRepository.findByPackageEntityId(id);
    packageServiceRepository.deleteAll(oldServices);

    Map<Long, Long> serviceCount = dto.getServiceIds().stream()
          .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

    List<PackageServiceEntity> newServices = new ArrayList<>();
    for (Map.Entry<Long, Long> entry : serviceCount.entrySet()) {
      TypeOfServiceEntity service = typeOfServiceRepository.findById(entry.getKey())
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado com id: " + entry.getKey()));

      PackageServiceEntity ps = new PackageServiceEntity();
      ps.setPackageEntity(packageEntity);
      ps.setService(service);
      ps.setQuantity(entry.getValue().intValue());

      newServices.add(ps);
    }
    packageServiceRepository.saveAll(newServices);

    return mapToDTO(packageEntity);
  }

  @Transactional
  public void deletePackage(Long id) {
    PackageEntity packageEntity = packageRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Pacote não encontrado"));

    List<PackageServiceEntity> packageServices = packageServiceRepository.findByPackageEntityId(id);
    packageServiceRepository.deleteAll(packageServices);

    packageRepository.delete(packageEntity);
  }

  private PackageDTO mapToDTO(PackageEntity entity) {
    PackageDTO dto = new PackageDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setTotalPrice(entity.getTotalPrice());
    dto.setNumberSessions(entity.getNumberSessions());
    dto.setOwnerId(entity.getOwner().getId());
    dto.setCreatedAt(entity.getCreatedAt());

    List<PackageServiceEntity> packageServices = packageServiceRepository.findByPackageEntityId(entity.getId());

    dto.setServices(packageServices.stream().map(ps -> {
      TypeOfServiceDTO serviceDto = new TypeOfServiceDTO();
      serviceDto.setId(ps.getService().getId());
      serviceDto.setName(ps.getService().getName());
      serviceDto.setDescription(ps.getService().getDescription());
      serviceDto.setPrice(ps.getService().getPrice());
      serviceDto.setCategory(ps.getService().getCategory());
      serviceDto.setDuration(ps.getService().getDuration());
      serviceDto.setOwnerId(ps.getService().getOwner().getId());
      serviceDto.setCreatedAt(ps.getService().getCreatedAt());
      serviceDto.setQuantity(ps.getQuantity());
      return serviceDto;
    }).collect(Collectors.toList()));

    return dto;
  }
}