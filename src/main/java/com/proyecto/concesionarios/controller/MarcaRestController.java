package com.proyecto.concesionarios.controller;


import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.MarcaRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.proyecto.concesionarios.repository.ConcesionarioRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/marcas")
public class MarcaRestController {

    @Autowired
    private MarcaRepository marcaRepository;


    // Registrar nuevas marcas
    /**@PostMapping
    public ResponseEntity<?> registrarMarca(@RequestBody Marca marca) {
        // Validar que al menos dos atributos obligatorios estén presentes
        if (marca.getNombre() == null || marca.getPaisOrigen() == null) {
            // Manejo de error si no se cumplen los requisitos
            return ResponseEntity.badRequest().body("Los campos 'nombre' y 'pais' son obligatorios.");
        }
        Marca savedMarca = marcaRepository.save(marca);
        return ResponseEntity.ok(savedMarca);
    }*/
    @PostMapping
    public ResponseEntity<String> registrarMarca(
            @RequestParam String nombre,
            @RequestParam String paisOrigen,
            @RequestParam String sitioWeb,
            @RequestParam String telefono,
            @RequestParam String anyoFundacion,
            @RequestParam(required = false) String nombreModelo,
            @RequestParam(required = false) String tipoCoche,
            @RequestParam(required = false) int anyoLanzamiento,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) float precio,
            @RequestParam(required = false) String fechaFabricacion) {

        Marca nuevaMarca = new Marca();
        nuevaMarca.setNombre(nombre);
        nuevaMarca.setPaisOrigen(paisOrigen);
        nuevaMarca.setSitioWeb(sitioWeb);
        nuevaMarca.setTelefono(telefono);
        nuevaMarca.setAnyoFundacion(anyoFundacion);

        if (nombreModelo != null && tipoCoche != null && anyoLanzamiento != 0) {
            Modelo nuevoModelo = new Modelo();
            nuevoModelo.setNombre(nombreModelo);
            nuevoModelo.setTipoCoche(tipoCoche);
            nuevoModelo.setAnyoLanzamiento(anyoLanzamiento);
            nuevoModelo.setMarca(nuevaMarca);

            if (color != null && matricula != null && precio != 0 && fechaFabricacion != null) {
                Coche nuevoCoche = new Coche();
                nuevoCoche.setColor(color);
                nuevoCoche.setMatricula(matricula);
                nuevoCoche.setPrecio(precio);
                nuevoCoche.setFechaFabricacion(LocalDate.parse(fechaFabricacion));

                nuevoCoche.setModelo(nuevoModelo);
                nuevoModelo.setCoches(Collections.singletonList(nuevoCoche));
            }

            nuevaMarca.setModelos(Collections.singletonList(nuevoModelo));
        }

        marcaRepository.save(nuevaMarca);

        return ResponseEntity.ok("Marca registrada correctamente");
    }


    // Actualizar información de marcas existentes
    @PutMapping("/{id}")
    public ResponseEntity<Marca> actualizarMarca(@PathVariable Long id, @Valid @RequestBody Marca marca) {
        if (!marcaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        marca.setId(id); // Asignar ID para evitar que se cree un nuevo registro
        Marca updatedMarca = marcaRepository.save(marca);
        return ResponseEntity.ok(updatedMarca);
    }

    // Eliminar marcas
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Long id) {
        if (!marcaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        marcaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Consultar marcas disponibles, filtrando por cualquiera de sus atributos
    @GetMapping
    public ResponseEntity<List<Marca>> buscarMarcas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String sitioWeb,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String anyoFundacion)


    {

        List<Marca> marcas;
        if (nombre != null || paisOrigen != null || sitioWeb != null || telefono != null || anyoFundacion != null) {
            marcas = marcaRepository.findByNombreContainingAndPaisOrigenContainingAndSitioWebContainingAndTelefonoContainingAndAnyoFundacionContaining(
                    nombre != null ? nombre : "",
                    paisOrigen != null ? paisOrigen : "",
                    sitioWeb != null ? sitioWeb : "",
                    telefono != null ? telefono : "",
                    Integer.parseInt(anyoFundacion != null ? anyoFundacion : "")
            );
        } else {
            marcas = marcaRepository.findAll();
        }
        return ResponseEntity.ok(marcas);
    }

    // Devolver resultados de forma paginada
    @GetMapping("/paginado")
    public ResponseEntity<Page<Marca>> buscarMarcasPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Marca> pageOfMarcas = marcaRepository.findAll(pageable);
        return ResponseEntity.ok(pageOfMarcas);
    }
}
