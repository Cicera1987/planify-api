package com.tcc.planify_api.controller;

import com.tcc.planify_api.docs.ContactApi;
import com.tcc.planify_api.dto.contact.ContactCreateDTO;
import com.tcc.planify_api.dto.contact.ContactDTO;
import com.tcc.planify_api.service.ContactService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController implements ContactApi {

  private final ContactService contactService;

  @Override
  public ResponseEntity<List<ContactDTO>> listContact() {
    List<ContactDTO> contatos = contactService.getAllContacts();
    return ResponseEntity.ok(contatos);
  }

  @Override
  public ResponseEntity<ContactDTO> createContact(@Valid @RequestBody ContactCreateDTO contactCreateDTO) {
    ContactDTO created = contactService.createContact(contactCreateDTO);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<ContactDTO> updateContact(@NotNull @PathVariable("idContato") Long idContato,
                                                     @Valid @RequestBody ContactCreateDTO contactCreateDTO) throws Exception {
    ContactDTO updated = contactService.updateContact(idContato, contactCreateDTO);
    return ResponseEntity.ok(updated);
  }

  @Override
  public ResponseEntity<Void> deleteContact(@NotNull @PathVariable("idContato") Long idContato) throws Exception {
    contactService.deleteContact(idContato);
    return ResponseEntity.noContent().build();
  }

}

