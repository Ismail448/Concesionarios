package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.dto.CocheDTO;
import com.proyecto.concesionarios.dto.ModeloDTO;
import com.proyecto.concesionarios.dto.SearchRequestDTO;
import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.CocheRepository;
import com.proyecto.concesionarios.repository.MarcaRepository;
import com.proyecto.concesionarios.repository.ModeloRepository;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Hibernate;
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
import java.util.ArrayList;
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

    @PostMapping("modeloJson")
    public ResponseEntity<Object> registrarModeloJson(@RequestBody ModeloDTO modeloDTO) {
        // Validar que al menos dos atributos sean obligatorios
        if (modeloDTO.getNombre() == null || modeloDTO.getNombre().isEmpty() ||
                modeloDTO.getTipoCoche() == null || modeloDTO.getTipoCoche().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Se requieren al menos dos atributos obligatorios para el modelo.\"}");
        }

        Modelo nuevoModelo = new Modelo();
        nuevoModelo.setNombre(modeloDTO.getNombre());
        nuevoModelo.setTipoCoche(modeloDTO.getTipoCoche());
        nuevoModelo.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());

        // Guardar el modelo primero
        modeloRepository.save(nuevoModelo);

        // Verificar si se deben registrar coches para este modelo
        if (modeloDTO.getCoches() != null) {
            for (CocheDTO cocheDTO : modeloDTO.getCoches()) {
                // Validar que al menos dos atributos sean obligatorios para el coche
                if (cocheDTO.getColor() == null || cocheDTO.getColor().isEmpty() ||
                        cocheDTO.getMatricula() == null || cocheDTO.getMatricula().isEmpty()) {
                    return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para cada coche.");
                }
                Coche coche = new Coche();
                coche.setColor(cocheDTO.getColor());
                coche.setMatricula(cocheDTO.getMatricula());
                coche.setPrecio(cocheDTO.getPrecio());
                coche.setFechaFabricacion(cocheDTO.getFechaFabricacion());
                coche.setModelo(nuevoModelo);
                cocheRepository.save(coche);
            }
        }

        return ResponseEntity.ok("Modelo registrado correctamente con ID: " + nuevoModelo.getId());
    }

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
    @PutMapping("modeloJson/{id}")
    public ResponseEntity<Object> actualizarModeloJson(@PathVariable Long id, @RequestBody ModeloDTO modeloDTO) {
        // Validar que el modelo exista
        Optional<Modelo> optionalModelo = modeloRepository.findById(id);
        if (!optionalModelo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Modelo modeloExistente = optionalModelo.get();

        // Actualizar los atributos del modelo si se proporcionan
        if (modeloDTO.getNombre() != null && !modeloDTO.getNombre().isEmpty()) {
            modeloExistente.setNombre(modeloDTO.getNombre());
        }
        if (modeloDTO.getTipoCoche() != null && !modeloDTO.getTipoCoche().isEmpty()) {
            modeloExistente.setTipoCoche(modeloDTO.getTipoCoche());
        }
        if (modeloDTO.getAnyoLanzamiento() != null) {
            modeloExistente.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());
        }

        // Guardar el modelo actualizado
        modeloRepository.save(modeloExistente);

        // Devolver una respuesta con el modelo actualizado
        return ResponseEntity.ok("Modelo modificado correctamente.");
    }

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
    public ResponseEntity<String> eliminarModelo(@PathVariable Long id) {
        if (!modeloRepository.existsById(id)) {
            return ResponseEntity.ok("ID no encontrado, introduzca uno valido");
        }
        modeloRepository.deleteById(id);
        return ResponseEntity.ok("Modelo con ID " + id + " eliminado correctamente");
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

    @GetMapping("all")
    public List<Modelo> getAllModelosWithCoches() {
        return modeloRepository.findAllModelos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModeloById(@PathVariable Long id) {
        Optional<Modelo> modeloOptional = modeloRepository.findById(id);
        if (modeloOptional.isPresent()) {
            Modelo modelo = modeloOptional.get();
            ModeloDTO modeloDTO = new ModeloDTO(
                    modelo.getId(),
                    modelo.getNombre(),
                    modelo.getTipoCoche(),
                    modelo.getAnyoLanzamiento(),
                    modelo.getMarca().getId()
            );
            return ResponseEntity.ok(modeloDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modelo no encontrado por el ID proporcionado");
        }
    }


    @PostMapping("/modelos/search")
    public ResponseEntity<?> searchModelos(@RequestBody SearchRequestDTO request) {
        try {
            // Validar el tamaño de la página
            int pageSize = request.getPage().getPageSize();
            if (pageSize <= 0) {
                return ResponseEntity.badRequest().body("El tamaño de la página debe ser mayor que cero.");
            }

            // Construir criterios de orden
            Sort sort = buildSortCriteria(request.getListOrderCriteria());

            // Construir criterios de búsqueda
            Specification<Modelo> spec = buildSearchCriteria(request.getListSearchCriteria());

            // Paginación
            Pageable pageable = PageRequest.of(request.getPage().getPageIndex(), request.getPage().getPageSize(), sort);

            // Realizar la búsqueda paginada
            Page<Modelo> modelosPage = modeloRepository.findAll(spec, pageable);

            // Mapear los resultados a ModeloDTO
            Page<ModeloDTO> modelosDTOPage = modelosPage.map(modelo -> new ModeloDTO(
                    modelo.getId(),
                    modelo.getNombre(),
                    modelo.getTipoCoche(),
                    modelo.getAnyoLanzamiento(),
                    modelo.getMarca().getId()
            ));

            return new ResponseEntity<>(modelosDTOPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Sort buildSortCriteria(List<SearchRequestDTO.OrderCriteriaDTO> listOrderCriteria) {
        List<Sort.Order> orders = new ArrayList<>();
        for (SearchRequestDTO.OrderCriteriaDTO orderCriteria : listOrderCriteria) {
            Sort.Direction direction = orderCriteria.getSortBy().equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(new Sort.Order(direction, orderCriteria.getValueSortOrder()));
        }
        return Sort.by(orders);
    }

    private Specification<Modelo> buildSearchCriteria(List<SearchRequestDTO.SearchCriteriaDTO> listSearchCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchRequestDTO.SearchCriteriaDTO searchCriteria : listSearchCriteria) {
                switch (searchCriteria.getKey()) {
                    case "nombre", "tipoCoche" -> {
                        String normalizedValue = searchCriteria.getValue().toLowerCase();
                        if (searchCriteria.getOperation().equals("contains")) {
                            predicates.add(cb.like(cb.lower(root.get(searchCriteria.getKey())), "%" + normalizedValue + "%"));
                        } else if (searchCriteria.getOperation().equals("equal")) {
                            predicates.add(cb.equal(cb.lower(root.get(searchCriteria.getKey())), normalizedValue));
                        }
                    }
                    case "anyoLanzamiento" -> {
                        int year = Integer.parseInt(searchCriteria.getValue());
                        switch (searchCriteria.getOperation()) {
                            case "greater_than" ->
                                    predicates.add(cb.greaterThan(root.get(searchCriteria.getKey()), year));
                            case "equal" -> predicates.add(cb.equal(root.get(searchCriteria.getKey()), year));
                            case "lower_than" -> predicates.add(cb.lessThan(root.get(searchCriteria.getKey()), year));
                        }
                    }
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}

