package com.proyecto.concesionarios.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CocheDTO {
    private int id;
    private String color;
    private String matricula;
    private float precio;
    private LocalDate fechaFabricacion;
}
