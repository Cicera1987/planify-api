package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import com.tcc.planify_api.entity.PositionEntity;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.enums.PositionEnum;
import com.tcc.planify_api.repository.PositionRepository;
import com.tcc.planify_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PositionRepository positionRepository;

  @Transactional
  public UserDTO createUser(UserCreateDTO userCreateDTO) {

    PositionEntity position = positionRepository.findByName(userCreateDTO.getPosition().name())
          .orElseThrow(() -> new IllegalArgumentException("Posição inválida: " + userCreateDTO.getPosition()));

    UserEntity userEntity = UserEntity.builder()
          .username(userCreateDTO.getUsername())
          .email(userCreateDTO.getEmail())
          .phone(userCreateDTO.getPhone())
          .password(userCreateDTO.getPassword())
          .speciality(userCreateDTO.getSpeciality())
          .position(position)
          .active(true)
          .build();

    UserEntity savedUser = userRepository.save(userEntity);
    return mapToDTO(savedUser);
  }

  @Transactional(readOnly = true)
  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream()
          .map(this::mapToDTO)
          .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public UserDTO getUserById(Long id) {
    UserEntity user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
    return mapToDTO(user);
  }

  private UserDTO mapToDTO(UserEntity userEntity) {
    return UserDTO.builder()
          .id(userEntity.getId())
          .username(userEntity.getUsername())
          .email(userEntity.getEmail())
          .phone(userEntity.getPhone())
          .password(userEntity.getPassword())
          .position(PositionEnum.valueOf(userEntity.getPosition().getName()))
          .speciality(userEntity.getSpeciality())
          .active(userEntity.isActive())
          .build();
  }
  @Transactional(readOnly = true)
  public UserDTO login(String email, String password) {
    UserEntity user = userRepository.findByEmail(email)
          .orElseThrow(() -> new IllegalArgumentException("Email não encontrado"));

    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("Senha incorreta");
    }

    return mapToDTO(user);
  }

}