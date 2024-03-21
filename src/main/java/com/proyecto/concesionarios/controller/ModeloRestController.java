package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.CocheRepository;
import com.proyecto.concesionarios.repository.MarcaRepository;
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

    @Autowired
    CocheRepository cocheRepository;

    @Autowired
    MarcaRepository marcaRepository;

    /**@PostMapping
    public ResponseEntity<?> registrarModelo(@RequestBody Modelo modelo) {
        // Validar que al menos dos atributos obligatorios estén presentes
        if (modelo.getNombre() == null || modelo.getTipoCoche() == null) {
            // Manejo de error si no se cumplen los requisitos
            return ResponseEntity.badRequest().body("Los campos 'nombre' y 'tipo de coche' son obligatorios.");
        }
        Modelo savedModelo = modeloRepository.save(modelo);
        return ResponseEntity.ok(savedModelo);
    }*/

    @PostMapping
    public ResponseEntity<String> registrarModelo(
            @RequestParam String nombre,
            @RequestParam String tipoCoche,
            @RequestParam int anyoLanzamiento,
            @RequestParam(required = false) List<Long> marcasIds) {

        Modelo nuevoModelo = new Modelo();
        nuevoModelo.setNombre(nombre);
        nuevoModelo.setTipoCoche(tipoCoche);
        nuevoModelo.setAnyoLanzamiento(anyoLanzamiento);

        if (marcasIds != null && !marcasIds.isEmpty()) {
            List<Marca> marcas = marcaRepository.findAllById(marcasIds);
            // Solo puedes asignar una marca a un modelo, por lo tanto, debes seleccionar una de la lista
            if (!marcas.isEmpty()) {
                nuevoModelo.setMarca(marcas.get(0));
            } else {
                // Manejar la situación en la que no se encuentren marcas
                return ResponseEntity.badRequest().body("No se encontraron marcas con los IDs proporcionados");
            }
        }

        modeloRepository.save(nuevoModelo);

        return ResponseEntity.ok("Modelo registrado correctamente");
    }

    //Actualizar información de concesionarios existentes
    /**@PutMapping("/{id}")
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
    }*/

    @PutMapping("{id}")
    public ResponseEntity<String> actualizarModelo(
            @PathVariable Long id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCoche,
            @RequestParam(required = false) Integer anyoLanzamiento,
            @RequestParam(required = false) Long marcaId) {

        Optional<Modelo> optionalModelo = modeloRepository.findById(id);
        if (!optionalModelo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Modelo modelo = optionalModelo.get();

        // Actualizar solo los campos que se proporcionen en la solicitud
        if (nombre != null) {
            modelo.setNombre(nombre);
        }
        if (tipoCoche != null) {
            modelo.setTipoCoche(tipoCoche);
        }
        if (anyoLanzamiento != null) {
            modelo.setAnyoLanzamiento(anyoLanzamiento);
        }
        if (marcaId != null) {
            Optional<Marca> marcaOptional = marcaRepository.findById(marcaId);
            if (marcaOptional.isPresent()) {
                modelo.setMarca(marcaOptional.get());
            } else {
                return ResponseEntity.badRequest().body("No se encontró una marca con el ID proporcionado: " + marcaId);
            }
        }

        modeloRepository.save(modelo);

        return ResponseEntity.ok("Modelo actualizado correctamente");
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
