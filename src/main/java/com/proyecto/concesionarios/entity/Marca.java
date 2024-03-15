package com.proyecto.concesionarios.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String paisOrigen;
    private String sitioWeb;
    private String telefono;
    private String anyoFundacion;

    @ManyToMany(mappedBy = "marcas")
    private Set<Concesionario> concesionarios = new HashSet<>();
}