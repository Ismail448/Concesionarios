package com.proyecto.concesionarios.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String tipoCoche;
    private int anyoLanzamiento;

    @ManyToOne
    @JoinColumn(name = "idMarca")
    private Marca marca;

    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL)
    private Set<Coche> coches = new HashSet<>();
}
