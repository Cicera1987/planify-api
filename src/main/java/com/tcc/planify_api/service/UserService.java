package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import com.tcc.planify_api.entity.PositionEntity;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.enums.PositionEnum;
import com.tcc.planify_api.repository.PositionRepository;
import com.tcc.planify_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PositionRepository positionRepository;

  public Optional<UserEntity> findByLogin(String username) {
    return userRepository.findByUsername(username);
  }

  @Transactional
  public UserDTO createUser(UserCreateDTO userCreateDTO) {
    PositionEnum positionEnum = Optional.ofNullable(userCreateDTO.getPosition())
          .orElse(PositionEnum.PROFESSIONAL);

    PositionEntity position = positionRepository.findByPosition(positionEnum)
          .orElseThrow(() -> new IllegalArgumentException("Posição inválida: " + positionEnum));

    UserEntity userEntity = UserEntity.builder()
          .username(userCreateDTO.getUsername())
          .email(userCreateDTO.getEmail())
          .phone(userCreateDTO.getPhone())
          .password(passwordEncoder.encode(userCreateDTO.getPassword()))
          .speciality(userCreateDTO.getSpeciality())
          .position(position)
          .active(Boolean.TRUE.equals(userCreateDTO.getActive()))
          .build();

    return mapToUserDTO(userRepository.save(userEntity));
  }

  @Transactional(readOnly = true)
  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream()
          .map(this::mapToUserDTO)
          .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public UserDTO getUserById(Long id) {
    UserEntity user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
    return mapToUserDTO(user);
  }

  @Transactional
  public UserDTO updateUser(Long id, UserCreateDTO updateDTO) {
    UserEntity user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));

    if (updateDTO.getUsername() != null) user.setUsername(updateDTO.getUsername());
    if (updateDTO.getEmail() != null) user.setEmail(updateDTO.getEmail());
    if (updateDTO.getPhone() != null) user.setPhone(updateDTO.getPhone());
    if (updateDTO.getSpeciality() != null) user.setSpeciality(updateDTO.getSpeciality());
    if (updateDTO.getPosition() != null) {
      PositionEntity position = positionRepository.findByPosition(updateDTO.getPosition())
            .orElseThrow(() -> new IllegalArgumentException("Cargo inválido: " + updateDTO.getPosition()));
      user.setPosition(position);
    }
    if (updateDTO.getActive() != null) user.setActive(updateDTO.getActive());
    if (updateDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));

    return mapToUserDTO(userRepository.save(user));
  }

  @Transactional(readOnly = true)
  public UserDTO login(String email, String rawPassword) {
    UserEntity user = userRepository.findByEmail(email)
          .orElseThrow(() -> new IllegalArgumentException("Email não encontrado"));

    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new IllegalArgumentException("Senha incorreta");
    }

    return mapToUserDTO(user);
  }

  private UserDTO mapToUserDTO(UserEntity userEntity) {
    return UserDTO.builder()
          .id(userEntity.getId())
          .username(userEntity.getUsername())
          .email(userEntity.getEmail())
          .phone(userEntity.getPhone())
          .position(userEntity.getPosition().getPosition())
          .speciality(userEntity.getSpeciality())
          .active(userEntity.isActive())
          .build();
  }
}