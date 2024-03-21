package com.proyecto.concesionarios.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModeloDTO {
    private Long id;
    private String nombre;
    private String tipoCoche;
    private Integer anyoLanzamiento;
    private Long marcaId;
    private List<CocheDTO> coches;
}
