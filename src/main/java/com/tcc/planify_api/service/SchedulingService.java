package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.scheduling.SchedulingCreateDTO;
import com.tcc.planify_api.dto.scheduling.SchedulingDTO;
import com.tcc.planify_api.entity.*;
import com.tcc.planify_api.enums.StatusAgendamento;
import com.tcc.planify_api.repository.*;
import com.tcc.planify_api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulingService {

  private final SchedulingRepository schedulingRepository;
  private final PackageServiceRepository packageServiceRepository;
  private final ContactRepository contactRepository;
  private final UserRepository userRepository;
  private final TypeOfServiceRepository typeOfServiceRepository;
  private final CalendarTimeRepository calendarTimeRepository;
  private final CalendarDayRepository calendarDayRepository;

  @Transactional(readOnly = true)
  public List<SchedulingDTO> getActiveSchedulingsForProfessional() {
    Long professionalId = AuthUtil.getAuthenticatedProfessionalId();

    List<String> activeStatuses = Arrays.asList(
          StatusAgendamento.AGENDADO.getDescription(),
          StatusAgendamento.CONFIRMADO.getDescription(),
          StatusAgendamento.EM_ANDAMENTO.getDescription(),
          StatusAgendamento.REMARCADO.getDescription()
    );

    List<SchedulingEntity> schedulings = schedulingRepository.findActiveSchedulings(professionalId, activeStatuses);

    return schedulings.stream()
          .map(this::mapToDTO)
          .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<SchedulingDTO> getSchedulingHistory(LocalDate startDate, LocalDate endDate, List<String> statuses) {
    Long professionalId = AuthUtil.getAuthenticatedProfessionalId();

    List<SchedulingEntity> schedulings = schedulingRepository.findSchedulingHistory(professionalId, startDate, endDate, statuses);

    return schedulings.stream()
          .map(this::mapToDTO)
          .collect(Collectors.toList());
  }

  @Transactional
  public SchedulingDTO createScheduling(SchedulingCreateDTO dto) {
    Long professionalId = AuthUtil.getAuthenticatedProfessionalId();

    // Profissional
    UserEntity professional = userRepository.findById(professionalId)
          .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));

    // Contato (garante que pertence ao profissional logado)
    ContactEntity contact = contactRepository.findById(dto.getContactId())
          .orElseThrow(() -> new IllegalArgumentException("Contato não encontrado"));
    if (!contact.getProfessional().getId().equals(professionalId)) {
      throw new IllegalArgumentException("Contato não pertence ao profissional logado");
    }

    // Dia e Horário
    CalendarDayEntity calendarDay = calendarDayRepository.findById(dto.getCalendarDayId())
          .orElseThrow(() -> new IllegalArgumentException("Dia não encontrado"));
    CalendarTimeEntity calendarTime = calendarTimeRepository.findById(dto.getCalendarTimeId())
          .orElseThrow(() -> new IllegalArgumentException("Horário não encontrado"));

    // Verifica conflito no horário
    boolean existsConflict = schedulingRepository.existsByCalendarTimeAndProfessional(calendarTime, professional);
    if (existsConflict) {
      throw new IllegalArgumentException("Horário já ocupado");
    }

    // Cria agendamento
    SchedulingEntity scheduling = SchedulingEntity.builder()
          .contact(contact)
          .professional(professional)
          .calendarDay(calendarDay)
          .calendarTime(calendarTime)
          .status(StatusAgendamento.AGENDADO.getDescription())
          .createdAt(LocalDateTime.now())
          .build();

    // Serviços
    if (dto.getServiceId() != null && !dto.getServiceId().isEmpty()) {
      List<TypeOfServiceEntity> services = typeOfServiceRepository.findAllById(dto.getServiceId());

      if (services.size() != dto.getServiceId().size()) {
        throw new IllegalArgumentException("Um ou mais serviços não foram encontrados");
      }

      if (dto.getPackageId() != null) {
        for (TypeOfServiceEntity service : services) {
          debitPackageItem(dto.getPackageId(), service.getId());

          PackageServiceEntity ps = packageServiceRepository
                .findByPackageEntityIdAndServiceId(dto.getPackageId(), service.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                      "Serviço " + service.getName() + " não disponível no pacote"));

          scheduling.addService(ps.getService());
          scheduling.setPackageEntity(ps.getPackageEntity());
        }
      } else {
        for (TypeOfServiceEntity service : services) {
          scheduling.addService(service);
        }
      }
    } else if (dto.getPackageId() != null) {
      throw new IllegalArgumentException("Deve informar ao menos um serviço dentro do pacote");
    }

    scheduling = schedulingRepository.save(scheduling);
    return mapToDTO(scheduling);
  }


  @Transactional
  public SchedulingDTO updateStatus(Long schedulingId, StatusAgendamento newStatus) {
    SchedulingEntity scheduling = schedulingRepository.findById(schedulingId)
          .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

    String oldStatus = scheduling.getStatus();
    boolean wasConcluded = oldStatus.equals(StatusAgendamento.CONCLUIDO.getDescription());
    boolean willBeConcluded = newStatus == StatusAgendamento.CONCLUIDO;

    // Debitar ou restaurar quantidade do pacote
    if (!wasConcluded && willBeConcluded && scheduling.getPackageEntity() != null) {
      debitPackageItem(scheduling.getPackageEntity().getId(), scheduling.getService().getId());
    } else if (wasConcluded && !willBeConcluded && scheduling.getPackageEntity() != null) {
      restorePackageItem(scheduling.getPackageEntity().getId(), scheduling.getService().getId());
    }

    scheduling.setStatus(newStatus.getDescription());
    schedulingRepository.save(scheduling);

    return mapToDTO(scheduling);
  }

  @Transactional
  public void deleteScheduling(Long schedulingId) {
    SchedulingEntity scheduling = schedulingRepository.findById(schedulingId)
          .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

    if (scheduling.getStatus().equals(StatusAgendamento.CONCLUIDO.getDescription())
          && scheduling.getPackageEntity() != null) {
      restorePackageItem(scheduling.getPackageEntity().getId(), scheduling.getService().getId());
    }

    schedulingRepository.delete(scheduling);
  }


  private void debitPackageItem(Long packageId, Long serviceId) {
    PackageServiceEntity ps = packageServiceRepository.findByPackageEntityIdAndServiceId(packageId, serviceId)
          .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado no pacote"));

    if (ps.getQuantity() <= 0)
      throw new IllegalArgumentException("Quantidade do serviço esgotada no pacote");

    ps.setQuantity(ps.getQuantity() - 1);
    packageServiceRepository.save(ps);
  }

  private void restorePackageItem(Long packageId, Long serviceId) {
    PackageServiceEntity ps = packageServiceRepository.findByPackageEntityIdAndServiceId(packageId, serviceId)
          .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado no pacote"));

    ps.setQuantity(ps.getQuantity() + 1);
    packageServiceRepository.save(ps);
  }

  private SchedulingDTO mapToDTO(SchedulingEntity entity) {
    SchedulingDTO dto = new SchedulingDTO();
    dto.setId(entity.getId());
    dto.setContactId(entity.getContact().getId());
    dto.setPackageId(entity.getPackageEntity() != null ? entity.getPackageEntity().getId() : null);
    dto.setCalendarTimeId(entity.getCalendarTime().getId());
    dto.setStatus(entity.getStatus());
    dto.setCreatedAt(entity.getCreatedAt());

    if (entity.getServices() != null) {
      List<Long> serviceIds = entity.getServices().stream()
            .map(TypeOfServiceEntity::getId)
            .toList();
      dto.setServiceId(serviceIds);
    }
    return dto;
  }


}