package com.proyecto.concesionarios.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipoCoche;
    private int anyoLanzamiento;

    @ManyToMany(mappedBy = "modelos", fetch = FetchType.LAZY)
    private List<Marca> marcas;

    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Coche> coches;
}
