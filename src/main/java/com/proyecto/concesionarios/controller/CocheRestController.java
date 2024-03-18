package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.CocheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coches")
public class CocheRestController {

    @Autowired
    CocheRepository cocheRepository;

    @PostMapping
    public ResponseEntity<Coche> registrarCoche(@RequestBody Coche coche) {
        // Validar que al menos dos atributos obligatorios estén presentes
        if (coche.getColor() == null || coche.getMatricula() == null) {
            // Manejo de error si no se cumplen los requisitos
            return ResponseEntity.badRequest().build();
        }
        Coche savedCoche = cocheRepository.save(coche);
        return ResponseEntity.ok(savedCoche);
    }

    //Actualizar información de concesionarios existentes
    @PutMapping("/{id}")
    public ResponseEntity<Coche> actualizarCoche(@PathVariable Long id, @RequestBody Coche coche) {
        Optional<Coche> optionalCoche = cocheRepository.findById(id);
        if (!optionalCoche.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Coche existingCoche = optionalCoche.get();
        // Actualizar la información del concesionario
        existingCoche.setColor(coche.getColor());
        existingCoche.setMatricula(coche.getMatricula());
        existingCoche.setPrecio(coche.getPrecio());
        existingCoche.setFechaFabricacion(coche.getFechaFabricacion());
        // Actualizar otros campos según sea necesario

        Coche updatedCoche = cocheRepository.save(existingCoche);
        return ResponseEntity.ok(updatedCoche);
    }

    //Eliminar concesionarios
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCoche(@PathVariable Long id) {
        if (!cocheRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cocheRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Consultar concesionarios disponibles, filtrando por cualquiera de sus atributos
    @GetMapping
    public ResponseEntity<List<Coche>> buscarCoche(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) Double precio,
            @RequestParam(required = false) LocalDate fechaFabricacion)

            {

        List<Coche> coches;
        if (color != null || matricula != null || precio != null || fechaFabricacion != null) {
            coches = cocheRepository.findCocheByColorContainingAndMatriculaContainingAndPrecioAndFechaFabricacion(
                    color != null ? color : "",
                    matricula != null ? matricula : "",
                    precio != null ? precio : 0,
                    fechaFabricacion != null ? fechaFabricacion : LocalDate.parse("")
            );
        } else {
            coches = cocheRepository.findAll();
        }

        return ResponseEntity.ok(coches);
    }



    //Devolver resultados de forma paginada
    @GetMapping("/paginado")
    public ResponseEntity<Page<Coche>> buscarCochesPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Coche> pageOfCoches = cocheRepository.findAll(pageable);
        return ResponseEntity.ok(pageOfCoches);
    }
}
