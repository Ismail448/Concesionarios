package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.entity.Concesionario;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ConcesionarioRepository extends JpaRepository<Concesionario, Long> {
    List<Concesionario> findByNombreContainingAndDireccionContainingAndTelefonoContainingAndEmailContainingAndSitioWebContaining(
            String nombre,
            String direccion,
            String telefono,
            String email,
            String sitioWeb
    );


}


