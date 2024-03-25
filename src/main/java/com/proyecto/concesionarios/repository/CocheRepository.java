package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.dto.CocheDTO;
import com.proyecto.concesionarios.entity.Coche;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface CocheRepository extends JpaRepository<Coche, Long>, JpaSpecificationExecutor<Coche> {

    List<Coche> findCocheByColorContainingAndMatriculaContainingAndPrecioAndFechaFabricacion(
            String color,
            String matricula,
            Float precio,
            LocalDate fechaFabricacion
    );

    Page<Coche> findAll(Specification<Coche> spec, Pageable pageable);
}
