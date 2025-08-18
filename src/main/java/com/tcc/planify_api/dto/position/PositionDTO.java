package com.tcc.planify_api.dto.position;

import com.tcc.planify_api.enums.PositionEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionDTO {
  private PositionEnum position;
}