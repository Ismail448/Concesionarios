package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    List<Modelo> findByNombreContainingAndTipoCocheContainingAndAnyoLanzamiento(
            String nombre,
            String tipoCoche,
            int anyoLanzamiento
    );
}
