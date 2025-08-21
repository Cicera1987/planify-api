package com.tcc.planify_api.docs;

import com.tcc.planify_api.dto.scheduling.SchedulingCreateDTO;
import com.tcc.planify_api.dto.scheduling.SchedulingDTO;
import com.tcc.planify_api.enums.StatusAgendamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Agendamento")
@RequestMapping("/scheduling")
public interface SchedulingApi {

  @Operation(summary = "Listar agendamentos ativos", description = "Retorna a lista de agendamentos ativos para o profissional logado, ordenados por dia e horário.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping("/active")
  ResponseEntity<List<SchedulingDTO>> getActiveSchedulings();

  @Operation(summary = "Listar histórico de agendamentos", description = "Retorna a lista de agendamentos concluídos ou com status específicos em um intervalo de datas para o profissional logado.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @GetMapping("/history")
  ResponseEntity<List<SchedulingDTO>> getSchedulingHistory(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        @RequestParam List<String> statuses
  );

  @Operation(summary = "Criar um novo agendamento", description = "Cria um novo agendamento para o profissional logado. Pode ser avulso ou vinculado a um pacote.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. Os dados fornecidos não são válidos ou quantidade do pacote esgotada."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PostMapping
  ResponseEntity<SchedulingDTO> createScheduling(@Valid @RequestBody SchedulingCreateDTO schedulingCreateDTO);

  @Operation(summary = "Atualizar status do agendamento", description = "Atualiza o status de um agendamento existente para o profissional logado.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida. ID ou status fornecido não é válido."),
        @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @PutMapping("/{idScheduling}/status")
  ResponseEntity<SchedulingDTO> updateStatus(
        @NotNull @PathVariable("idScheduling") Long idScheduling,
        @RequestParam StatusAgendamento newStatus
  );

  @Operation(summary = "Deletar um agendamento", description = "Exclui o agendamento com o ID fornecido para o profissional logado. Restaura quantidade do pacote se necessário.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Agendamento excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
  })
  @DeleteMapping("/{idScheduling}")
  ResponseEntity<Void> deleteScheduling(@NotNull @PathVariable("idScheduling") Long idScheduling);
}