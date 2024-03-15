package com.proyecto.concesionarios.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Coche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;
    private String matricula;
    private double precio;
    private String fechaFabricacion;

    @ManyToOne
    @JoinColumn(name = "id_modelo")
    private Modelo modelo;

}