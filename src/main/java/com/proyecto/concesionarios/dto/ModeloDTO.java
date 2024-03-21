package com.proyecto.concesionarios.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModeloDTO {
    private int id;
    private String nombre;
    private String tipoCoche;
    private int anyoLanzamiento;
    private Long marcaId;
    private List<CocheDTO> coches;
}
