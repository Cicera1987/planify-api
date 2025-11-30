package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.notification.NotificationTokenDTO;
import com.tcc.planify_api.entity.ContactEntity;
import com.tcc.planify_api.entity.NotificationTokenEntity;
import com.tcc.planify_api.repository.ContactRepository;
import com.tcc.planify_api.repository.NotificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationTokenService {

  private final NotificationTokenRepository repository;
  private final ContactRepository contactRepository;

  public NotificationTokenService(NotificationTokenRepository repository,
                                  ContactRepository contactRepository) {
    this.repository = repository;
    this.contactRepository = contactRepository;
  }

  public void saveToken(NotificationTokenDTO dto) {

    // evita duplicado
    NotificationTokenEntity existing = repository.findByToken(dto.getToken());
    if (existing != null) return;

    // busca contato
    ContactEntity contact = contactRepository.findById(dto.getContactId())
          .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

    NotificationTokenEntity token = NotificationTokenEntity.builder()
          .token(dto.getToken())
          .contact(contact)
          .deviceInfo(dto.getDeviceInfo())
          .build();

    repository.save(token);
  }
  public List<NotificationTokenEntity> getTokensByContact(Long contactId) {
    return repository.findByContactId(contactId);
  }

}