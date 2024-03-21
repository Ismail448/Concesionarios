package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.CocheRepository;
import com.proyecto.concesionarios.repository.ModeloRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coches")
public class CocheRestController {

    @Autowired
    CocheRepository cocheRepository;

    @Autowired
    ModeloRepository modeloRepository;

    /**@PostMapping
    public ResponseEntity<?> registrarCoche(@RequestBody Coche coche) {
        // Validar que al menos dos atributos obligatorios estén presentes
        if (coche.getColor() == null || coche.getMatricula() == null) {
            // Manejo de error si no se cumplen los requisitos
            return ResponseEntity.badRequest().body("Los campos 'color' y 'matricula' son obligatorios.");
        }
        Coche savedCoche = cocheRepository.save(coche);
        return ResponseEntity.ok(savedCoche);
    }*/

    @PostMapping
    public ResponseEntity<String> registrarCoche(
            @RequestParam String color,
            @RequestParam String matricula,
            @RequestParam float precio,
            @RequestParam String fechaFabricacion,
            @RequestParam Long modeloId) throws NotFoundException {

        Modelo modelo = modeloRepository.findById(modeloId)
                .orElseThrow(() -> new NotFoundException("Modelo no encontrado"));

        if (color != null && matricula != null && precio != 0 && fechaFabricacion != null) {
            Coche nuevoCoche = new Coche();
            nuevoCoche.setColor(color);
            nuevoCoche.setMatricula(matricula);
            nuevoCoche.setPrecio(precio);
            nuevoCoche.setFechaFabricacion(LocalDate.parse(fechaFabricacion, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            nuevoCoche.setModelo(modelo);
            modelo.setCoches(Collections.singletonList(nuevoCoche));

            cocheRepository.save(nuevoCoche);

            return ResponseEntity.ok("Coche registrado correctamente");
        } else {
            return ResponseEntity.badRequest().body("Por favor, proporcione todos los campos requeridos");
        }
    }


    //Actualizar información de concesionarios existentes
    /**@PutMapping("/{id}")
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
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCoche(
            @PathVariable Long id,
            @RequestParam String color,
            @RequestParam String matricula,
            @RequestParam float precio,
            @RequestParam String fechaFabricacion,
            @RequestParam(required = false) Long modeloId) {

        Optional<Coche> optionalCoche = cocheRepository.findById(id);
        if (!optionalCoche.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Coche coche = optionalCoche.get();
        coche.setColor(color);
        coche.setMatricula(matricula);
        coche.setPrecio(precio);

        // Parsear la fecha de fabricación
        LocalDate fecha = null;
        try {
            fecha = LocalDate.parse(fechaFabricacion);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha de fabricación no válido");
        }
        coche.setFechaFabricacion(fecha);

        if (modeloId != null) {
            Optional<Modelo> modeloOptional = modeloRepository.findById(modeloId);
            if (modeloOptional.isPresent()) {
                coche.setModelo(modeloOptional.get());
            } else {
                return ResponseEntity.badRequest().body("No se encontró un modelo con el ID proporcionado: " + modeloId);
            }
        } else {
            coche.setModelo(null); // Si no se proporciona un nuevo ID de modelo, eliminamos la asociación existente
        }

        cocheRepository.save(coche);

        return ResponseEntity.ok("Coche actualizado correctamente");
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
            @RequestParam(required = false) Float precio,
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
