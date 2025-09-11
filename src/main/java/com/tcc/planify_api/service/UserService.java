package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.Image.ImageSourceRequest;
import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.dto.user.UserCreateDTO;
import com.tcc.planify_api.dto.user.UserDTO;
import com.tcc.planify_api.dto.user.UserUpdateDTO;
import com.tcc.planify_api.entity.PositionEntity;
import com.tcc.planify_api.entity.UserEntity;
import com.tcc.planify_api.enums.PositionEnum;
import com.tcc.planify_api.repository.PositionRepository;
import com.tcc.planify_api.repository.UserRepository;
import com.tcc.planify_api.util.ApiVersionProvider;
import com.tcc.planify_api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final PositionRepository positionRepository;
  private final ApiVersionProvider versionProvider;
  private final ImageProviderService imageProviderService;

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

    String imageUrl = imageProviderService.getImageUrl(
          ImageSourceRequest.builder()
                .file(userCreateDTO.getFile())
                .externalUrl(userCreateDTO.getImageUrl())
                .build()
    );
    userEntity.setImageUrl(imageUrl);

    return mapToUserDTO(userRepository.save(userEntity));
  }

  @Transactional(readOnly = true)
  public PageDTO<UserDTO> getAllUsers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());

    Page<UserEntity> users = userRepository.findAll(pageable);

    return PaginationUtil.toPageResponse(users, this::mapToUserDTO, versionProvider.getVersion());
  }

  @Transactional(readOnly = true)
  public PageDTO<UserDTO> getSearchUsers(String name, String speciality, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());

    Page<UserEntity> users = userRepository.searchUsers(name, speciality, pageable);

    return PaginationUtil.toPageResponse(users, this::mapToUserDTO,  versionProvider.getVersion());
  }

  @Transactional(readOnly = true)
  public UserDTO getUserById(Long id) {
    UserEntity user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
    return mapToUserDTO(user);
  }

  @Transactional
  public UserDTO updateUser(Long id, UserUpdateDTO updateDTO) {
    UserEntity user = userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

    if (updateDTO.getUsername() != null) user.setUsername(updateDTO.getUsername());
    if (updateDTO.getEmail() != null) user.setEmail(updateDTO.getEmail());
    if (updateDTO.getPhone() != null) user.setPhone(updateDTO.getPhone());
    if (updateDTO.getSpeciality() != null) user.setSpeciality(updateDTO.getSpeciality());

    String imageUrl = imageProviderService.getImageUrl(
          ImageSourceRequest.builder()
                .file(updateDTO.getFile())
                .externalUrl(updateDTO.getImageUrl())
                .build()
    );
    user.setImageUrl(imageUrl);

    if (isAdmin) {
      if (updateDTO.getPosition() != null) {
        PositionEnum positionEnum = updateDTO.getPosition();
        PositionEntity position = positionRepository.findByPosition(positionEnum)
              .orElseThrow(() -> new IllegalArgumentException("Cargo inválido: " + positionEnum));
        user.setPosition(position);
      }
      if (updateDTO.getActive() != null) {
        user.setActive(updateDTO.getActive());
      }
    }
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

  @Transactional(readOnly = true)
  public UserEntity getUserEntityById(Long id) {
    return userRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
  }

  private UserDTO mapToUserDTO(UserEntity userEntity) {
    return UserDTO.builder()
          .id(userEntity.getId())
          .username(userEntity.getUsername())
          .email(userEntity.getEmail())
          .phone(userEntity.getPhone())
          .position(userEntity.getPosition().getPosition())
          .speciality(userEntity.getSpeciality())
          .imageUrl(userEntity.getImageUrl())
          .active(userEntity.isActive())
          .build();
  }
}