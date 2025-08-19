package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.contact.ContactCreateDTO;
import com.tcc.planify_api.dto.contact.ContactDTO;
import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.entity.ContactEntity;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.repository.ContactRepository;
import com.tcc.planify_api.repository.UserRepository;
import com.tcc.planify_api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

  private final ContactRepository contactRepository;
  private final UserRepository userRepository;

  public PageDTO<ContactDTO> getContacts(Pageable pageable) {
    Page<ContactEntity> page = contactRepository.findAll(pageable);
    return PaginationUtil.toPageResponse(page, this::toDTO);
  }

  public PageDTO<ContactDTO> searchContacts(String name, Pageable pageable) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Long professionalId = Long.parseLong(auth.getPrincipal().toString());

    Page<ContactEntity> page = contactRepository.findByProfessionalIdAndNameContainingIgnoreCase(professionalId, name, pageable);
    return PaginationUtil.toPageResponse(page, this::toDTO);
  }

  @Transactional
  public ContactDTO createContact(ContactCreateDTO dto) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Long professionalId = Long.parseLong(auth.getPrincipal().toString());

    UserEntity professional = userRepository.findById(professionalId)
          .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado com id: " + professionalId));

    ContactEntity entity = ContactEntity.builder()
          .name(dto.getName())
          .phone(dto.getPhone())
          .email(dto.getEmail() != null ? dto.getEmail() : null)
          .observation(dto.getObservation() != null ? dto.getObservation() : null)
          .professional(professional)
          .createdAt(LocalDateTime.now())
          .build();

    entity = contactRepository.save(entity);
    return toDTO(entity);
  }

  public ContactDTO updateContact(Long id, ContactCreateDTO dto) throws Exception {
    ContactEntity entity = contactRepository.findById(id)
          .orElseThrow(() -> new Exception("Contato não encontrado"));

    entity.setName(dto.getName());
    entity.setPhone(dto.getPhone());
    entity.setEmail(dto.getEmail());
    entity.setObservation(dto.getObservation());

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
    dto.setObservation(entity.getObservation());
    dto.setProfessionalId(entity.getProfessional().getId());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }
}
