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
import com.tcc.planify_api.exception.RegraDeNegocioException;
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

  private final NotificationService notificationService;
  private final NotificationTokenService notificationTokenService;
  private final NotificationHistoryService notificationHistoryService;
  private final ClientPackageServiceRepository clientPackageServiceRepository;
  private final ClientPackageRepository clientPackageRepository;


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

    // Buscar profissional
    UserEntity professional = userRepository.findById(professionalId)
          .orElseThrow(() -> new RegraDeNegocioException("Profissional não encontrado"));

    // Buscar contato
    ContactEntity contact = contactRepository.findById(dto.getContactId())
          .orElseThrow(() -> new RegraDeNegocioException("Contato não encontrado"));

    if (!contact.getProfessional().getId().equals(professionalId)) {
      throw new RegraDeNegocioException("Contato não pertence ao profissional logado");
    }

    // Buscar dia e horário
    CalendarDayEntity calendarDay = calendarDayRepository.findById(dto.getCalendarDayId())
          .orElseThrow(() -> new RegraDeNegocioException("Dia não encontrado"));

    CalendarTimeEntity calendarTime = calendarTimeRepository.findById(dto.getCalendarTimeId())
          .orElseThrow(() -> new RegraDeNegocioException("Horário não encontrado"));

    // Checar conflito de horário
    boolean existsConflict = schedulingRepository.existsByCalendarTimeAndProfessional(calendarTime, professional);
    if (existsConflict) {
      throw new RegraDeNegocioException("Horário já ocupado");
    }

    // Criar agendamento
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
        throw new RegraDeNegocioException("Um ou mais serviços não foram encontrados");
      }

      // Pacote do cliente
      if (dto.getClientPackageId() != null) {
        ClientPackageEntity clientPackage = clientPackageRepository.findById(dto.getClientPackageId())
              .orElseThrow(() -> new RegraDeNegocioException("Pacote do cliente não encontrado"));

        scheduling.setClientPackage(clientPackage);

        for (TypeOfServiceEntity service : services) {
          // Debitar quantidade
          ClientPackageServiceEntity cps = clientPackage.getServices().stream()
                .filter(s -> s.getService().getId().equals(service.getId()))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException(
                      "Serviço " + service.getName() + " não disponível no pacote do cliente"
                ));

          if (cps.getQuantity() <= 0) {
            throw new RegraDeNegocioException(
                  "Quantidade do serviço " + service.getName() + " esgotada no pacote do cliente"
            );
          }

          cps.setQuantity(cps.getQuantity() - 1);
          clientPackageServiceRepository.save(cps);

          // Adicionar serviço no agendamento
          scheduling.addService(cps.getService());
        }

      } else {
        // Sem pacote
        for (TypeOfServiceEntity service : services) {
          scheduling.addService(service);
        }
      }

    } else if (dto.getClientPackageId() != null) {
      throw new RegraDeNegocioException("Deve informar ao menos um serviço dentro do pacote");
    }

    // Salvar agendamento
    scheduling = schedulingRepository.save(scheduling);

    // Notificações
    String title = "Novo agendamento";
    String body = "Um novo agendamento foi marcado para "
          + calendarDay.getLocalDate()
          + " às " + calendarTime.getTime();

    List<NotificationTokenEntity> tokens = notificationTokenService.getTokensByContact(contact.getId());
    for (NotificationTokenEntity t : tokens) {
      notificationService.sendNotification(t.getToken(), title, body);
    }
    notificationHistoryService.saveNotification(contact.getId(), title, body);

    return mapToDTOWithContact(scheduling);
  }

  @Transactional
  public SchedulingDTO updateStatus(Long schedulingId, StatusAgendamento newStatus) {

    SchedulingEntity scheduling = schedulingRepository.findById(schedulingId)
          .orElseThrow(() -> new RegraDeNegocioException("Agendamento não encontrado"));

    String oldStatus = scheduling.getStatus();
    boolean wasConcluded = oldStatus.equals(StatusAgendamento.CONCLUIDO.getDescription());
    boolean willBeConcluded = newStatus == StatusAgendamento.CONCLUIDO;

    // Debitar ou restaurar quantidade do pacote do cliente (caso exista)
    if (scheduling.getClientPackage() != null && scheduling.getServices() != null) {
      for (TypeOfServiceEntity service : scheduling.getServices()) {
        if (!wasConcluded && willBeConcluded) {
          debitClientPackageItem(scheduling.getClientPackage().getId(), service.getId());
        } else if (wasConcluded && !willBeConcluded) {
          restoreClientPackageItem(scheduling.getClientPackage().getId(), service.getId());
        }
      }
    }

    // Atualiza status
    scheduling.setStatus(newStatus.getDescription());
    schedulingRepository.save(scheduling);

    // Envio das notificações
    ContactEntity contact = scheduling.getContact();
    CalendarDayEntity calendarDay = scheduling.getCalendarDay();
    CalendarTimeEntity calendarTime = scheduling.getCalendarTime();

    List<NotificationTokenEntity> tokens =
          notificationTokenService.getTokensByContact(contact.getId());

    String title = "Atualização no agendamento";
    String body;

    switch (newStatus) {
      case CONFIRMADO -> body = "Agendamento confirmado para " + calendarDay.getLocalDate() + " às " + calendarTime.getTime();
      case CANCELADO -> body = "O agendamento em " + calendarDay.getLocalDate() + " às " + calendarTime.getTime() + " foi cancelado.";
      case REMARCADO -> body = "Agendamento remarcado para " + calendarDay.getLocalDate() + " às " + calendarTime.getTime();
      case CONCLUIDO -> body = "Agendamento concluído!";
      default -> body = "O status do agendamento foi atualizado.";
    }

    for (NotificationTokenEntity t : tokens) {
      notificationService.sendNotification(t.getToken(), title, body);
    }

    notificationHistoryService.saveNotification(contact.getId(), title, body);

    return mapToDTOWithContact(scheduling);
  }


  @Transactional
  public void deleteScheduling(Long schedulingId) {
    SchedulingEntity scheduling = schedulingRepository.findById(schedulingId)
          .orElseThrow(() -> new RegraDeNegocioException("Agendamento não encontrado"));

    if (scheduling.getStatus().equals(StatusAgendamento.CONCLUIDO.getDescription())
          && scheduling.getClientPackage() != null
          && scheduling.getServices() != null) {
      for (TypeOfServiceEntity service : scheduling.getServices()) {
        restoreClientPackageItem(scheduling.getClientPackage().getId(), service.getId());
      }
    }

    schedulingRepository.delete(scheduling);
  }

  @Transactional
  public void debitClientPackageItem(Long clientPackageId, Long serviceId) {
    ClientPackageEntity clientPackage = clientPackageRepository.findById(clientPackageId)
          .orElseThrow(() -> new IllegalArgumentException("Pacote do cliente não encontrado"));

    ClientPackageServiceEntity cps = clientPackage.getServices().stream()
          .filter(s -> s.getService().getId().equals(serviceId))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Serviço não disponível no pacote do cliente"));

    if (cps.getQuantity() <= 0) {
      throw new IllegalArgumentException("Quantidade do serviço esgotada no pacote do cliente");
    }

    cps.setQuantity(cps.getQuantity() - 1);
    clientPackageServiceRepository.save(cps);
  }

  @Transactional
  public void restoreClientPackageItem(Long clientPackageId, Long serviceId) {
    ClientPackageEntity clientPackage = clientPackageRepository.findById(clientPackageId)
          .orElseThrow(() -> new IllegalArgumentException("Pacote do cliente não encontrado"));

    ClientPackageServiceEntity cps = clientPackage.getServices().stream()
          .filter(s -> s.getService().getId().equals(serviceId))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Serviço não disponível no pacote do cliente"));

    cps.setQuantity(cps.getQuantity() + 1);
    clientPackageServiceRepository.save(cps);
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