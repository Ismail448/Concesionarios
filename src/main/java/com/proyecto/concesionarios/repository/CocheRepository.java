package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.entity.Coche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CocheRepository extends JpaRepository<Coche, Long> {

    List<Coche> findCocheByColorContainingAndMatriculaContainingAndPrecioAndFechaFabricacion(
            String color,
            String matricula,
            Float precio,
            LocalDate fechaFabricacion
    );

}
