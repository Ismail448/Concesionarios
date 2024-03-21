package com.proyecto.concesionarios.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarcaDTO {
    private Long id;
    private String nombre;
    private String paisOrigen;
    private String sitioWeb;
    private String telefono;
    private String anyoFundacion;
    private ModeloDTO modelo;
    private List<ModeloDTO> modelos;
}
