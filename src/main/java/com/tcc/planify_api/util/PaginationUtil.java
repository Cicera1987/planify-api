package com.tcc.planify_api.util;
import com.tcc.planify_api.dto.pagination.PageDTO;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginationUtil {
  public static <E, D> PageDTO<D> toPageResponse(Page<E> page, Function<E, D> converter) {
    List<D> content = page.stream()
          .map(converter)
          .collect(Collectors.toList());

    return new PageDTO<>(
          content,
          page.getNumber() + 1,
          page.getSize(),
          (int) page.getTotalElements(),
          page.getTotalPages()
    );
  }
}