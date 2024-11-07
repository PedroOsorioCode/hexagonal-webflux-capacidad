package com.microservicio.hxcapacidad.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadPaginacionResponseDto<T> {
    public static final String FIRST_PAGE_NUM = "0";

    List<T> content;
    int pageNumber;
    int pageSize;
    long totalElements;

    @JsonProperty
    public long totalPages() {
        return pageSize > 0 ? (totalElements - 1) / pageSize + 1 : 0;
    }

    @JsonProperty
    public boolean first() {
        return pageNumber == Integer.parseInt(FIRST_PAGE_NUM);
    }

    @JsonProperty
    public boolean last() {
        return (long) (pageNumber + 1) * pageSize >= totalElements;
    }
}

