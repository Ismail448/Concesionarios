package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.entity.Coche;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CocheRepository extends JpaRepository<Coche, Long> {

    List<Coche> findCocheByColorContainingAndMatriculaContainingAndPrecioAndFechaFabricacion(
            String color,
            String matricula,
            Double precio,
            LocalDate fechaFabricacion
    );
}
