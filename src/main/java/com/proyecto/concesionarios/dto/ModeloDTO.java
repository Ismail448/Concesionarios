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

    public ModeloDTO(Long id, String nombre, String tipoCoche, Integer anyoLanzamiento, Long marcaId) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCoche = tipoCoche;
        this.anyoLanzamiento = anyoLanzamiento;
        this.marcaId = marcaId;
    }
}
