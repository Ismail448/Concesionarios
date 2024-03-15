package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.entity.Concesionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface ConcesionarioRepository extends JpaRepository<Concesionario, Long> {
    public ArrayList<Concesionario> findAll();

    public Concesionario save(Concesionario concesionario);
    List<Concesionario> findByNombreContainingAndDireccionContainingAndTelefonoContainingAndEmailContainingAndSitioWebContaining(
            String nombre,
            String direccion,
            String telefono,
            String email,
            String sitioWeb
    );

    Page<Concesionario> findAll(Pageable pageable);
}


