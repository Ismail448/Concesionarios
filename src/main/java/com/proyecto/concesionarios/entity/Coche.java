package com.proyecto.concesionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Coche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;
    private String matricula;
    private Float precio;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/YYYY")
    private LocalDate fechaFabricacion;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@JsonIgnoreProperties("coches")
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

}
