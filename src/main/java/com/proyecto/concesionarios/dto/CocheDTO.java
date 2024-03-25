package com.proyecto.concesionarios.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CocheDTO {
    private Long id;
    private String color;
    private String matricula;
    private Float precio;
    private LocalDate fechaFabricacion;
    private Long modeloId;

    public CocheDTO(Long id,String color,String matricula, Float precio, LocalDate fechaFabricacion, Long modeloId) {
        this.id = id;
        this.color = color;
        this.matricula = matricula;
        this.precio = precio;
        this.fechaFabricacion = fechaFabricacion;
        this.modeloId = modeloId;
    }
}
