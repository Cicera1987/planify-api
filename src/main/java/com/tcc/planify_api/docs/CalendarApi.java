package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.calendar.CalendarDayCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarDayDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeCreateDTO;
import com.tcc.planify_api.dto.calendar.CalendarTimeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Calendário")
@RequestMapping("/calendar")
public interface CalendarApi {

  @Operation(summary = "Listar dias do calendário", description = "Retorna todos os dias cadastrados no calendário do profissional logado, incluindo os horários disponíveis de cada dia.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping
  ResponseEntity<List<CalendarDayDTO>> listDays();

  @Operation(summary = "Criar um novo dia no calendário", description = "Adiciona um novo dia com horários disponíveis ao calendário do profissional logado.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Dia do calendário criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Dados fornecidos não são válidos."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PostMapping
  ResponseEntity<List<CalendarDayDTO>> createDay(
        @Valid @RequestBody List<CalendarDayCreateDTO> calendarDayCreateDTOs);


  @Operation(summary = "Atualizar um dia do calendário", description = "Atualiza a data ou horários disponíveis de um dia específico do calendário.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dia atualizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida."),
        @ApiResponse(responseCode = "404", description = "Dia não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PutMapping("/{idDay}")
  ResponseEntity<CalendarDayDTO> updateDay(
        @NotNull @PathVariable("idDay") Long idDay,
        @Valid @RequestBody CalendarDayCreateDTO calendarDayCreateDTO) throws Exception;

  @Operation(summary = "Deletar um dia do calendário", description = "Remove um dia específico do calendário do profissional logado.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Dia excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Dia não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @DeleteMapping("/{idDay}")
  ResponseEntity<Void> deleteDay(@NotNull @PathVariable("idDay") Long idDay) throws Exception;

  @Operation(summary = "Adicionar horário em um dia", description = "Adiciona um novo horário disponível a um dia específico do calendário.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Horário adicionado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida."),
        @ApiResponse(responseCode = "404", description = "Dia não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PostMapping("/{idDay}/times")
  ResponseEntity<CalendarTimeDTO> addTime(
        @NotNull @PathVariable("idDay") Long idDay,
        @Valid @RequestBody CalendarTimeCreateDTO calendarTimeCreateDTO);

  @Operation(summary = "Deletar horário de um dia", description = "Remove um horário específico de um dia do calendário.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Horário removido com sucesso."),
        @ApiResponse(responseCode = "404", description = "Horário ou dia não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @DeleteMapping("/{idDay}/times/{idTime}")
  ResponseEntity<Void> deleteTime(
        @NotNull @PathVariable("idDay") Long idDay,
        @NotNull @PathVariable("idTime") Long idTime) throws Exception;
}
