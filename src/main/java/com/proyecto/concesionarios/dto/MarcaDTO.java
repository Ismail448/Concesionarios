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

    public MarcaDTO(Long id, String nombre, String paisOrigen, String sitioWeb, String telefono, String anyoFundacion) {
        this.id = id;
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.sitioWeb = sitioWeb;
        this.telefono = telefono;
        this.anyoFundacion = anyoFundacion;
    }
}
