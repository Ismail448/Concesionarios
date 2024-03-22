package com.proyecto.concesionarios.repository;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    List<Marca> findByNombreContainingAndPaisOrigenContainingAndSitioWebContainingAndTelefonoContainingAndAnyoFundacionContaining(
            String nombre,
            String paisOrigen,
            String sitioWeb,
            String telefono,
            int anyoFundacion
    );

    @Query("SELECT DISTINCT m FROM Marca m LEFT JOIN FETCH m.modelos")
    List<Marca> findAllMarcas();
}
