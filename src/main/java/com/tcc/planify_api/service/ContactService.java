package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.Image.ImageSourceRequest;
import com.tcc.planify_api.dto.contact.ContactCreateDTO;
import com.tcc.planify_api.dto.contact.ContactDTO;
import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.entity.ContactEntity;
import com.tcc.planify_api.entity.PackageEntity;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.repository.ContactRepository;
import com.tcc.planify_api.repository.PackageRepository;
import com.tcc.planify_api.repository.UserRepository;
import com.tcc.planify_api.util.ApiVersionProvider;
import com.tcc.planify_api.util.AuthUtil;
import com.tcc.planify_api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

  private final ContactRepository contactRepository;
  private final UserRepository userRepository;
  private final PackageRepository packageRepository;
  private final ApiVersionProvider versionProvider;
  private final ImageProviderService imageProviderService;

  public PageDTO<ContactDTO> getContacts(Pageable pageable) {
    Page<ContactEntity> page = contactRepository.findAll(pageable);
    return PaginationUtil.toPageResponse(page, this::toDTO, versionProvider.getVersion());
  }

  public PageDTO<ContactDTO> searchContacts(String name, Pageable pageable) {
    Long professionalId = AuthUtil.getAuthenticatedUserId();

    Page<ContactEntity> page = contactRepository.findByProfessionalIdAndNameContainingIgnoreCase(professionalId, name, pageable);
    return PaginationUtil.toPageResponse(page, this::toDTO, versionProvider.getVersion());
  }

  @Transactional
  public ContactDTO getContactById(Long id) throws Exception {
    ContactEntity contact = contactRepository.findById(id)
          .orElseThrow(() -> new Exception("Contato não encontrado "));
    return toDTO(contact);
  }

  @Transactional
  public ContactDTO createContact(ContactCreateDTO dto) {
    Long professionalId = AuthUtil.getAuthenticatedUserId();

    UserEntity professional = userRepository.findById(professionalId)
          .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado com id: " + professionalId));

    ContactEntity entity = ContactEntity.builder()
          .name(dto.getName())
          .phone(dto.getPhone())
          .email(dto.getEmail())
          .observation(dto.getObservation())
          .gender(dto.getGender())
          .professional(professional)
          .createdAt(LocalDateTime.now())
          .build();

    String imageUrl = imageProviderService.getImageUrl(
          ImageSourceRequest.builder()
                .file(dto.getFile())
                .externalUrl(dto.getImageUrl())
                .build()
    );
    entity.setImageUrl(imageUrl);

    if (dto.getPackageIds() != null && !dto.getPackageIds().isEmpty()) {
      List<PackageEntity> packages = packageRepository.findAllById(dto.getPackageIds());
      entity.setPackages(packages);
    }

    entity = contactRepository.save(entity);
    return toDTO(entity);
  }


  @Transactional
  public ContactDTO updateContact(Long id, ContactCreateDTO dto) throws Exception {
    ContactEntity entity = contactRepository.findById(id)
          .orElseThrow(() -> new Exception("Contato não encontrado"));

    entity.setName(dto.getName());
    entity.setPhone(dto.getPhone());
    entity.setEmail(dto.getEmail());
    entity.setGender(dto.getGender());
    entity.setObservation(dto.getObservation());

    String imageUrl = imageProviderService.getImageUrl(
          ImageSourceRequest.builder()
                .file(dto.getFile())
                .externalUrl(dto.getImageUrl())
                .build()
    );
    entity.setImageUrl(imageUrl);

    if (dto.getPackageIds() != null && !dto.getPackageIds().isEmpty()) {
      List<PackageEntity> packages = packageRepository.findAllById(dto.getPackageIds());
      entity.setPackages(packages);
    } else {
      entity.getPackages().clear();
    }

    entity = contactRepository.save(entity);
    return toDTO(entity);
  }


  public void deleteContact(Long id) {
    contactRepository.deleteById(id);
  }

  private ContactDTO toDTO(ContactEntity entity) {
    ContactDTO dto = new ContactDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setPhone(entity.getPhone());
    dto.setEmail(entity.getEmail());
    dto.setImageUrl(entity.getImageUrl());
    dto.setGender(entity.getGender());
    dto.setObservation(entity.getObservation());
    dto.setProfessionalId(entity.getProfessional().getId());
    dto.setCreatedAt(entity.getCreatedAt());

    dto.setPackageIds(
          entity.getPackages().stream()
                .map(PackageEntity::getId)
                .toList()
    );
    return dto;
  }
}
