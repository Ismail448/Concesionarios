package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/modelos")
public class ModeloRestController {

    @Autowired
    ModeloRepository modeloRepository;

    @PostMapping
    public ResponseEntity<Modelo> registrarModelo(@RequestBody Modelo modelo) {
        // Validar que al menos dos atributos obligatorios estén presentes
        if (modelo.getNombre() == null || modelo.getTipoCoche() == null) {
            // Manejo de error si no se cumplen los requisitos
            return ResponseEntity.badRequest().build();
        }
        Modelo savedModelo = modeloRepository.save(modelo);
        return ResponseEntity.ok(savedModelo);
    }

    //Actualizar información de concesionarios existentes
    @PutMapping("/{id}")
    public ResponseEntity<Modelo> actualizarModelo(@PathVariable Long id, @RequestBody Modelo modelo) {
        Optional<Modelo> optionalModelo = modeloRepository.findById(id);
        if (!optionalModelo.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Modelo existingModelo = optionalModelo.get();
        // Actualizar la información del concesionario
        existingModelo.setNombre(modelo.getNombre());
        existingModelo.setTipoCoche(modelo.getTipoCoche());
        existingModelo.setAnyoLanzamiento(modelo.getAnyoLanzamiento());
        // Actualizar otros campos según sea necesario

        Modelo updatedModelo = modeloRepository.save(existingModelo);
        return ResponseEntity.ok(updatedModelo);
    }

    //Eliminar concesionarios
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarModelo(@PathVariable Long id) {
        if (!modeloRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        modeloRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Consultar concesionarios disponibles, filtrando por cualquiera de sus atributos
    @GetMapping
    public ResponseEntity<List<Modelo>> buscarModelo(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCoche,
            @RequestParam(required = false) Integer anyoLanzamiento) {

        List<Modelo> modelos;
        if (nombre != null || tipoCoche != null || anyoLanzamiento != null) {
            modelos = modeloRepository.findByNombreContainingAndTipoCocheContainingAndAnyoLanzamiento(
                    nombre != null ? nombre : "",
                    tipoCoche != null ? tipoCoche : "",
                    anyoLanzamiento != null ? anyoLanzamiento : 0
            );
        } else {
            modelos = modeloRepository.findAll();
        }

        return ResponseEntity.ok(modelos);
    }



    //Devolver resultados de forma paginada
    @GetMapping("/paginado")
    public ResponseEntity<Page<Modelo>> buscarModelosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Modelo> pageOfModelos = modeloRepository.findAll(pageable);
        return ResponseEntity.ok(pageOfModelos);
    }
}
