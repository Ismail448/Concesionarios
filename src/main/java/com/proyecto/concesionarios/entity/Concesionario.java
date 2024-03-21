package com.proyecto.concesionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Concesionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String sitioWeb;

    //@JsonIgnoreProperties("concesionarios")
    //@JsonIgnore
    //@JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "concesionario_marca",
            joinColumns = @JoinColumn(name = "concesionario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "marca_id", referencedColumnName = "id"))
    private List<Marca> marcas;


}
