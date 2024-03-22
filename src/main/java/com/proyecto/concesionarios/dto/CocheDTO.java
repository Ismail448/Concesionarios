package com.proyecto.concesionarios.dto;

import com.proyecto.concesionarios.entity.Modelo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CocheDTO {
    private Long id;
    private String color;
    private String matricula;
    private float precio;
    private LocalDate fechaFabricacion;
    private Long modeloId;
}
