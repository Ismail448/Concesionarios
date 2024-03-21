package com.proyecto.concesionarios.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String paisOrigen;
    private String sitioWeb;
    private String telefono;
    private String anyoFundacion;

    @JsonIgnoreProperties({"marcas", "modelos"})
    //@JsonIgnore
    //@JsonBackReference
    @ManyToMany(mappedBy = "marcas")
    private List<Concesionario> concesionarios;

    @JsonIgnoreProperties("marca")
    //@JsonIgnore
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Modelo> modelos;

}