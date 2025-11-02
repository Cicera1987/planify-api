package com.tcc.planify_api.service;

import com.tcc.planify_api.dto.calendar.CalendarDayDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeDTO;
import com.tcc.planify_api.dto.contact.ContactDTO;
import com.tcc.planify_api.dto.packageServices.PackageDTO;
import com.tcc.planify_api.dto.pagination.PageDTO;
import com.tcc.planify_api.dto.scheduling.SchedulingCreateDTO;
import com.tcc.planify_api.dto.scheduling.SchedulingDTO;
import com.tcc.planify_api.dto.typeOfService.TypeOfServiceDTO;
import com.tcc.planify_api.entity.*;
import com.tcc.planify_api.enums.StatusAgendamento;
import com.tcc.planify_api.repository.*;
import com.tcc.planify_api.util.ApiVersionProvider;
import com.tcc.planify_api.util.AuthUtil;
import com.tcc.planify_api.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
  private final ApiVersionProvider versionProvider;

  @Transactional(readOnly = true)
  public PageDTO<SchedulingDTO> getActiveSchedulingsForProfessional(Pageable pageable) {
    Long professionalId = AuthUtil.getAuthenticatedUserId();

    List<String> activeStatuses = Arrays.asList(
          StatusAgendamento.AGENDADO.getDescription(),
          StatusAgendamento.CONFIRMADO.getDescription(),
          StatusAgendamento.EM_ANDAMENTO.getDescription(),
          StatusAgendamento.REMARCADO.getDescription()
    );

    Page<SchedulingEntity> page = schedulingRepository.findActiveSchedulings(professionalId, activeStatuses, pageable);

    return PaginationUtil.toPageResponse(page, this::mapToDTOWithContact, versionProvider.getVersion());
  }

  @Transactional(readOnly = true)
  public PageDTO<SchedulingDTO> getSchedulingHistory(LocalDate startDate, LocalDate endDate, List<String> statuses, Pageable pageable) {
    Long professionalId = AuthUtil.getAuthenticatedUserId();

    Page<SchedulingEntity> page = schedulingRepository.findSchedulingHistory(professionalId, startDate, endDate, statuses, pageable);

    return PaginationUtil.toPageResponse(page, this::mapToDTOWithContact, versionProvider.getVersion());
  }

  @Transactional
  public SchedulingDTO createScheduling(SchedulingCreateDTO dto) {
    Long professionalId = AuthUtil.getAuthenticatedUserId();

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
    return mapToDTOWithContact(scheduling);
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

    return mapToDTOWithContact(scheduling);
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

  @Transactional(readOnly = true)
  public PageDTO<SchedulingDTO> searchSchedulingsByContactName(String name, Pageable pageable) {
    Long professionalId = AuthUtil.getAuthenticatedUserId();

    Page<SchedulingEntity> page = schedulingRepository.findByContactName(professionalId, name, pageable);

    return PaginationUtil.toPageResponse(page, this::mapToDTOWithContact, versionProvider.getVersion());
  }

  private SchedulingDTO mapToDTOWithContact(SchedulingEntity entity) {
    SchedulingDTO dto = new SchedulingDTO();
    dto.setId(entity.getId());

    // Contato
    ContactEntity contact = entity.getContact();
    ContactDTO contactDTO = new ContactDTO();
    contactDTO.setId(contact.getId());
    contactDTO.setName(contact.getName());
    contactDTO.setEmail(contact.getEmail());
    contactDTO.setPhone(contact.getPhone());
    contactDTO.setImageUrl(contact.getImageUrl());
    contactDTO.setObservation(contact.getObservation());
    dto.setContact(contactDTO);

    // Pacote
    PackageEntity pkg = entity.getPackageEntity();
    if (pkg != null) {
      PackageDTO pkgDTO = new PackageDTO();
      pkgDTO.setId(pkg.getId());
      pkgDTO.setName(pkg.getName());
      dto.setPackageInfo(pkgDTO);
    }
    // Dia
    CalendarDayEntity day = entity.getCalendarDay();
    CalendarDayDTO dayDTO = new CalendarDayDTO();
    dayDTO.setId(day.getId());
    dayDTO.setUserId(day.getUserId());
    dayDTO.setLocalDate(day.getLocalDate());
    dto.setCalendarDay(dayDTO);

    // Horário
    CalendarTimeEntity time = entity.getCalendarTime();
    CalendarTimeDTO timeDTO = new CalendarTimeDTO();
    timeDTO.setId(time.getId());
    timeDTO.setTime(time.getTime());
    dto.setCalendarTime(timeDTO);

    dto.setStatus(entity.getStatus());
    dto.setCreatedAt(entity.getCreatedAt());

    // Serviços
    if (entity.getServices() != null && !entity.getServices().isEmpty()) {
      List<TypeOfServiceDTO> serviceDTOs = entity.getServices().stream().map(service -> {
        TypeOfServiceDTO s = new TypeOfServiceDTO();
        s.setId(service.getId());
        s.setName(service.getName());
        s.setPrice(service.getPrice());
        s.setDuration(service.getDuration());
        return s;
      }).toList();
      dto.setServices(serviceDTOs);
    }
    return dto;
  }
}