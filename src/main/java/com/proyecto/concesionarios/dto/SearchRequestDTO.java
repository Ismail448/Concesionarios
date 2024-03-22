package com.proyecto.concesionarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDTO {
    private List<OrderCriteriaDTO> listOrderCriteria;
    private List<SearchCriteriaDTO> listSearchCriteria;
    private PageDTO page;

    @Data
    public static class OrderCriteriaDTO {
        private String sortBy;
        private String valueSortOrder;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchCriteriaDTO {
        private String key;
        private String operation;
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageDTO {
        private int pageIndex;
        private int pageSize;
    }
}
