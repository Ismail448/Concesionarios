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
public class Concesionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String sitioWeb;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "concesionario_marca",
            joinColumns = @JoinColumn(name = "id_concesionario", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_marca", referencedColumnName = "id"))
    private List<Marca> marcas;


}
