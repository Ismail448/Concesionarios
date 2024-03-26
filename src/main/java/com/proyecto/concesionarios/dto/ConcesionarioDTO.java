package com.proyecto.concesionarios.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConcesionarioDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String sitioWeb;
    private List<MarcaDTO> marcas;

    public ConcesionarioDTO(Long id, String nombre, String direccion, String telefono, String email, String sitioWeb) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.sitioWeb = sitioWeb;
    }


}
