package com.proyecto.concesionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipoCoche;
    private int anyoLanzamiento;

    //@JsonIgnoreProperties("modelos")
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @JsonIgnoreProperties("modelo")
    //@JsonIgnore
    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coche> coches;
}
