package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.repository.ConcesionarioRepository;
import com.proyecto.concesionarios.service.ConcesionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/concesionario")
public class ConcesionarioRestController {

    @Autowired
    private ConcesionarioRepository concesionarioRepository;

    //Registrar nuevos concesionarios
    @PostMapping
    public ResponseEntity<Concesionario> registrarConcesionario(@RequestBody Concesionario concesionario) {
        // Validar que al menos dos atributos obligatorios estén presentes
        if (concesionario.getNombre() == null || concesionario.getDireccion() == null) {
            // Manejo de error si no se cumplen los requisitos
            return ResponseEntity.badRequest().build();
        }
        Concesionario savedConcesionario = concesionarioRepository.save(concesionario);
        return ResponseEntity.ok(savedConcesionario);
    }

    //Actualizar información de concesionarios existentes
    @PutMapping("/{id}")
    public ResponseEntity<Concesionario> actualizarConcesionario(@PathVariable Long id, @RequestBody Concesionario concesionario) {
        Optional<Concesionario> optionalConcesionario = concesionarioRepository.findById(id);
        if (!optionalConcesionario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Concesionario existingConcesionario = optionalConcesionario.get();
        // Actualizar la información del concesionario
        existingConcesionario.setNombre(concesionario.getNombre());
        existingConcesionario.setDireccion(concesionario.getDireccion());
        existingConcesionario.setTelefono(concesionario.getTelefono());
        existingConcesionario.setEmail(concesionario.getEmail());
        existingConcesionario.setSitioWeb(concesionario.getSitioWeb());
        // Actualizar otros campos según sea necesario

        Concesionario updatedConcesionario = concesionarioRepository.save(existingConcesionario);
        return ResponseEntity.ok(updatedConcesionario);
    }

    //Eliminar concesionarios
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConcesionario(@PathVariable Long id) {
        if (!concesionarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        concesionarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Consultar concesionarios disponibles, filtrando por cualquiera de sus atributos
    @GetMapping
    public ResponseEntity<List<Concesionario>> buscarConcesionarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sitioWeb) {

        List<Concesionario> concesionarios;
        if (nombre != null || direccion != null || telefono != null || email != null || sitioWeb != null) {
            concesionarios = concesionarioRepository.findByNombreContainingAndDireccionContainingAndTelefonoContainingAndEmailContainingAndSitioWebContaining(
                    nombre != null ? nombre : "",
                    direccion != null ? direccion : "",
                    telefono != null ? telefono : "",
                    email != null ? email : "",
                    sitioWeb != null ? sitioWeb : ""
            );
        } else {
            concesionarios = concesionarioRepository.findAll();
        }

        return ResponseEntity.ok(concesionarios);
    }


    //Devolver resultados de forma paginada
    @GetMapping("/paginado")
    public ResponseEntity<Page<Concesionario>> buscarConcesionariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Concesionario> pageOfConcesionarios = concesionarioRepository.findAll(pageable);
        return ResponseEntity.ok(pageOfConcesionarios);
    }
}
