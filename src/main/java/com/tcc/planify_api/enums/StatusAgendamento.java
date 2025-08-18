package com.tcc.planify_api.enums;

import lombok.Getter;

@Getter
public enum StatusAgendamento {
  AGENDADO(1, "Agendado"),
  CONFIRMADO(2, "Confirmado"),
  REJEITADO(3, "Rejeitado"),
  CANCELADO(4, "Cancelado"),
  EM_ANDAMENTO(5, "Em andamento"),
  CONCLUIDO(6, "Concluído"),
  NAO_COMPARECEU(7, "Não compareceu"),
  REMARCADO(8, "Remarcado");

  private final int codigo;
  private final String descricao;

  StatusAgendamento(int codigo, String descricao) {
    this.codigo = codigo;
    this.descricao = descricao;
  }
}
