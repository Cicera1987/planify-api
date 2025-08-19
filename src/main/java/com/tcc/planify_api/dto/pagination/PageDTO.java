package com.tcc.planify_api.dto.pagination;

import lombok.*;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
  private List<T> content;
  private  Integer pageNumber;
  private Integer pageSize;
  private  Integer totalElements;
  private  Integer totalPages;
}
