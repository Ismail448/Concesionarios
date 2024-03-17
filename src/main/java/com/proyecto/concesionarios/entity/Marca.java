package com.proyecto.concesionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String paisOrigen;
    private String sitioWeb;
    private String telefono;
    private String anyoFundacion;

    @JsonIgnore
    @ManyToMany(mappedBy = "marcas")
    private List<Concesionario> concesionarios;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "marca_modelo",
            joinColumns = @JoinColumn(name = "id_marca", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "i_modelo", referencedColumnName = "id"))
    private List<Modelo> modelos;

}