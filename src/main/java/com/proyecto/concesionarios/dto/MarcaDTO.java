package com.proyecto.concesionarios.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarcaDTO {
    private int id;
    private String nombre;
    private String paisOrigen;
    private String sitioWeb;
    private String telefono;
    private String anyoFundacion;
    private ModeloDTO modelo;
}
