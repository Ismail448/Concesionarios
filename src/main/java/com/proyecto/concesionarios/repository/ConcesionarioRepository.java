package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import jakarta.persistence.metamodel.SingularAttribute;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
    @Query("SELECT DISTINCT c FROM Concesionario c LEFT JOIN FETCH c.marcas")
    List<Concesionario> findAllWithMarcas();
}


