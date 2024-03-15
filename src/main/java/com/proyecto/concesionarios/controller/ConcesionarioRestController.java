package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.service.ConcesionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/concesionario")
public class ConcesionarioRestController {

    @Autowired
    private ConcesionarioService concesionarioService;
    // Registrar nuevos concesionarios
    @PostMapping
    public Concesionario registrarConcesionario(@RequestBody Concesionario concesionario) {
        // Validar que al menos dos atributos obligatorios estén presentes
        if (concesionario.getNombre() == null || concesionario.getDireccion() == null) {
            // Manejo de error si no se cumplen los requisitos
            // Aquí puedes lanzar una excepción personalizada o retornar un mensaje de error
            throw new IllegalArgumentException("Se requieren al menos el nombre y la dirección del concesionario.");
        }
        return concesionarioService.save(concesionario);
    }

    //Actualizar información de concesionarios existentes
    @PutMapping("/{id}")
    public Concesionario actualizarConcesionario(@PathVariable Long id, @RequestBody Concesionario newConcesionario) {
        return concesionarioService.updateConcesionario(id, newConcesionario);
    }

    //Eliminar concesionarios
    @DeleteMapping("/{id}")
    public void eliminarConcesionario(@PathVariable Long id) {
        concesionarioService.deleteConcesionario(id);
    }

    //Consultar concesionarios disponibles, filtrando por cualquiera de sus atributos
    @GetMapping
    public List<Concesionario> buscarConcesionarios(@RequestParam(required = false) String nombre,
                                                    @RequestParam(required = false) String direccion,
                                                    @RequestParam(required = false) String telefono,
                                                    @RequestParam(required = false) String email,
                                                    @RequestParam(required = false) String sitioWeb) {
        return concesionarioService.buscarConcesionarios(nombre, direccion, telefono, email, sitioWeb);
    }

    //Devolver resultados de forma paginada
    @GetMapping("/paginado")
    public Page<Concesionario> buscarConcesionariosPaginados(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return concesionarioService.buscarConcesionariosPaginados(pageable);
    }
}
