package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.dto.CocheDTO;
import com.proyecto.concesionarios.dto.SearchRequestDTO;
import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.CocheRepository;
import com.proyecto.concesionarios.repository.ModeloRepository;
import jakarta.persistence.criteria.Predicate;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

    @PostMapping("registrarCocheJson")
    public ResponseEntity<String> registrarCocheJson(@RequestBody CocheDTO cocheDTO) {
        try {
            Modelo modelo = modeloRepository.findById(cocheDTO.getModeloId())
                    .orElseThrow(() -> new NotFoundException("Modelo no encontrado"));

            if (cocheDTO.getColor() != null && cocheDTO.getMatricula() != null &&
                    cocheDTO.getPrecio() != 0 && cocheDTO.getFechaFabricacion() != null) {
                Coche nuevoCoche = new Coche();
                nuevoCoche.setColor(cocheDTO.getColor());
                nuevoCoche.setMatricula(cocheDTO.getMatricula());
                nuevoCoche.setPrecio(cocheDTO.getPrecio());
                nuevoCoche.setFechaFabricacion(cocheDTO.getFechaFabricacion());

                // Asigna el modelo al coche
                nuevoCoche.setModelo(modelo);

                // Guarda el coche y actualiza la relación en el modelo
                cocheRepository.save(nuevoCoche);
                modeloRepository.save(modelo);

                return ResponseEntity.ok("Coche registrado correctamente");
            } else {
                return ResponseEntity.badRequest().body("Por favor, proporcione todos los campos requeridos");
            }
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Modelo no encontrado");
        }
    }

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

    @PutMapping("CocheJson/{id}")
    public ResponseEntity<String> actualizarCocheJson(@PathVariable Long id, @RequestBody CocheDTO cocheDTO) {

        Optional<Coche> optionalCoche = cocheRepository.findById(id);
        if (!optionalCoche.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Coche coche = optionalCoche.get();

        // Actualizar solo los campos que no sean nulos en el DTO
        if (cocheDTO.getColor() != null) {
            coche.setColor(cocheDTO.getColor());
        }
        if (cocheDTO.getMatricula() != null) {
            coche.setMatricula(cocheDTO.getMatricula());
        }
        if (cocheDTO.getPrecio() != 0) {
            coche.setPrecio(cocheDTO.getPrecio());
        }
        if (cocheDTO.getFechaFabricacion() != null) {
            // Parsear la fecha de fabricación
            LocalDate fecha = null;
            try {
                fecha = cocheDTO.getFechaFabricacion();
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Formato de fecha de fabricación no válido");
            }
            coche.setFechaFabricacion(fecha);
        }
        if (cocheDTO.getModeloId() != null) {
            Optional<Modelo> modeloOptional = modeloRepository.findById(cocheDTO.getModeloId());
            if (modeloOptional.isPresent()) {
                coche.setModelo(modeloOptional.get());
            } else {
                return ResponseEntity.badRequest().body("No se encontró un modelo con el ID proporcionado: " + cocheDTO.getModeloId());
            }
        } else {
            coche.setModelo(null); // Si no se proporciona un nuevo ID de modelo, eliminamos la asociación existente
        }

        cocheRepository.save(coche);

        return ResponseEntity.ok("Coche actualizado correctamente");
    }

    //Actualizar información de concesionarios existentes
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCoche(
            @PathVariable Long id,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) Float precio,
            @RequestParam(required = false) String fechaFabricacion,
            @RequestParam(required = false) Long modeloId) {

        Optional<Coche> optionalCoche = cocheRepository.findById(id);
        if (!optionalCoche.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Coche coche = optionalCoche.get();

        // Verificar y actualizar los campos no nulos
        if (color != null) {
            coche.setColor(color);
        }
        if (matricula != null) {
            coche.setMatricula(matricula);
        }
        if (precio != null) {
            coche.setPrecio(precio);
        }
        if (fechaFabricacion != null) {
            try {
                LocalDate fecha = LocalDate.parse(fechaFabricacion);
                coche.setFechaFabricacion(fecha);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Formato de fecha de fabricación no válido");
            }
        }
        if (modeloId != null) {
            Optional<Modelo> modeloOptional = modeloRepository.findById(modeloId);
            if (modeloOptional.isPresent()) {
                coche.setModelo(modeloOptional.get());
            } else {
                return ResponseEntity.badRequest().body("No se encontró un modelo con el ID proporcionado: " + modeloId);
            }
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
            @RequestParam(required = false) LocalDate fechaFabricacion) {

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

    @GetMapping("all")
    public List<Coche> getAllCoches(){
        return cocheRepository.findAll();
    }

    @PostMapping("/coches/search")
    public ResponseEntity<Page<Coche>> searchCoches(@RequestBody SearchRequestDTO request) {
        // Construir criterios de orden
        Sort sort = buildSortCriteria(request.getListOrderCriteria());

        // Construir criterios de búsqueda
        Specification<Coche> spec = buildSearchCriteria(request.getListSearchCriteria());

        // Paginación
        Pageable pageable = PageRequest.of(request.getPage().getPageIndex(), request.getPage().getPageSize(), sort);

        // Realizar la búsqueda paginada
        Page<Coche> cochesPage = cocheRepository.findAll(spec, pageable);

        return new ResponseEntity<>(cochesPage, HttpStatus.OK);
    }

    private Sort buildSortCriteria(List<SearchRequestDTO.OrderCriteriaDTO> listOrderCriteria) {
        List<Sort.Order> orders = new ArrayList<>();
        for (SearchRequestDTO.OrderCriteriaDTO orderCriteria : listOrderCriteria) {
            Sort.Direction direction = orderCriteria.getSortBy().equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(new Sort.Order(direction, orderCriteria.getValueSortOrder()));
        }
        return Sort.by(orders);
    }

    private Specification<Coche> buildSearchCriteria(List<SearchRequestDTO.SearchCriteriaDTO> listSearchCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchRequestDTO.SearchCriteriaDTO searchCriteria : listSearchCriteria) {
                if (searchCriteria.getKey().equals("color")) {
                    predicates.add(cb.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%"));
                } else if (searchCriteria.getKey().equals("precio")) {
                    predicates.add(cb.equal(root.get(searchCriteria.getKey()), Float.parseFloat(searchCriteria.getValue())));
                } else if (searchCriteria.getKey().equals("fechaFabricacion")) {
                    predicates.add(cb.equal(root.get(searchCriteria.getKey()), LocalDate.parse(searchCriteria.getValue())));
                }
                // Agrega más casos según sea necesario para otros campos de búsqueda
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
