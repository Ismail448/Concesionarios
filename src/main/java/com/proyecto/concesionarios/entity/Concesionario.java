package com.proyecto.concesionarios.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Concesionario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String sitioWeb;

    @ManyToMany
    @JoinTable(
            name = "concesionario_marca",
            joinColumns = @JoinColumn(name = "idConcesionario"),
            inverseJoinColumns = @JoinColumn(name = "idMarca")
    )
    private Set<Marca> marcas = new HashSet<>();

}
