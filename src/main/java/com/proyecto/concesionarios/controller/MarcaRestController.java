package com.proyecto.concesionarios.controller;


import com.proyecto.concesionarios.dto.CocheDTO;
import com.proyecto.concesionarios.dto.MarcaDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.proyecto.concesionarios.repository.ConcesionarioRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/marcas")
public class MarcaRestController {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private CocheRepository cocheRepository;

    @Autowired
    private ConcesionarioRepository concesionarioRepository;

    // Registrar nuevas marcas y posibilidad de registrar modelos y coches tambien
    @PostMapping("MarcaJson")
    public ResponseEntity<String> registrarMarcaJson(@RequestBody MarcaDTO marcaDTO) {
        // Validar que al menos dos atributos sean obligatorios para la marca
        if (marcaDTO.getNombre() == null || marcaDTO.getNombre().isEmpty() ||
                marcaDTO.getPaisOrigen() == null || marcaDTO.getPaisOrigen().isEmpty()) {
            return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para la marca.");
        }

        Marca marca = new Marca();
        marca.setNombre(marcaDTO.getNombre());
        marca.setPaisOrigen(marcaDTO.getPaisOrigen());
        marca.setSitioWeb(marcaDTO.getSitioWeb());
        marca.setTelefono(marcaDTO.getTelefono());
        marca.setAnyoFundacion(marcaDTO.getAnyoFundacion());

        // Guardar la marca
        Marca savedMarca = marcaRepository.save(marca);

        // Verificar si se deben registrar modelos para esta marca
        if (marcaDTO.getModelos() != null) {
            for (ModeloDTO modeloDTO : marcaDTO.getModelos()) {
                // Validar que al menos dos atributos sean obligatorios para el modelo
                if (modeloDTO.getNombre() == null || modeloDTO.getNombre().isEmpty() ||
                        modeloDTO.getTipoCoche() == null || modeloDTO.getTipoCoche().isEmpty()) {
                    return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para el modelo.");
                }
                Modelo modelo = new Modelo();
                modelo.setNombre(modeloDTO.getNombre());
                modelo.setTipoCoche(modeloDTO.getTipoCoche());
                modelo.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());
                modelo.setMarca(savedMarca);
                modeloRepository.save(modelo);

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
                        coche.setModelo(modelo);
                        cocheRepository.save(coche);
                    }
                }
            }
        }

        return ResponseEntity.ok("Marca registrada correctamente.");
    }

    @PostMapping
    public ResponseEntity<String> registrarMarca(
            @RequestParam String nombre,
            @RequestParam String paisOrigen,
            @RequestParam String sitioWeb,
            @RequestParam String telefono,
            @RequestParam String anyoFundacion,
            @RequestParam(required = false) String nombreModelo,
            @RequestParam(required = false) String tipoCoche,
            @RequestParam(required = false) Integer anyoLanzamiento,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) Float precio,
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


    @PutMapping("/MarcaJson/{id}")
    public ResponseEntity<String> actualizarMarcaJson(@PathVariable Long id, @RequestBody MarcaDTO marcaDTO) {
        // Verificar si existe la marca con el ID proporcionado
        Optional<Marca> optionalMarca = marcaRepository.findById(id);
        if (optionalMarca.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Marca marca = optionalMarca.get();

        // Actualizar los atributos obligatorios de la marca
        if (marcaDTO.getNombre() != null && !marcaDTO.getNombre().isEmpty()) {
            marca.setNombre(marcaDTO.getNombre());
        }
        if (marcaDTO.getPaisOrigen() != null && !marcaDTO.getPaisOrigen().isEmpty()) {
            marca.setPaisOrigen(marcaDTO.getPaisOrigen());
        }

        // Actualizar los atributos opcionales de la marca
        if (marcaDTO.getSitioWeb() != null) {
            marca.setSitioWeb(marcaDTO.getSitioWeb());
        }
        if (marcaDTO.getTelefono() != null) {
            marca.setTelefono(marcaDTO.getTelefono());
        }
        if (marcaDTO.getAnyoFundacion() != null) {
            marca.setAnyoFundacion(marcaDTO.getAnyoFundacion());
        }

        // Guardar la marca actualizada
        marcaRepository.save(marca);

        // Verificar si se deben actualizar modelos para esta marca
        if (marcaDTO.getModelos() != null) {
            for (ModeloDTO modeloDTO : marcaDTO.getModelos()) {
                // Crear un nuevo modelo si no tiene ID o buscar el existente
                Modelo modelo;
                if (modeloDTO.getId() != null) {
                    Optional<Modelo> optionalModelo = modeloRepository.findById(modeloDTO.getId());
                    if (optionalModelo.isPresent()) {
                        modelo = optionalModelo.get();
                    } else {
                        modelo = new Modelo();
                        modelo.setMarca(marca);
                    }
                } else {
                    modelo = new Modelo();
                    modelo.setMarca(marca);
                }

                // Actualizar los atributos obligatorios del modelo
                if (modeloDTO.getNombre() != null && !modeloDTO.getNombre().isEmpty()) {
                    modelo.setNombre(modeloDTO.getNombre());
                }
                if (modeloDTO.getTipoCoche() != null && !modeloDTO.getTipoCoche().isEmpty()) {
                    modelo.setTipoCoche(modeloDTO.getTipoCoche());
                }

                // Actualizar los atributos opcionales del modelo
                if (modeloDTO.getAnyoLanzamiento() != null) {
                    modelo.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());
                }

                // Guardar el modelo actualizado
                modeloRepository.save(modelo);

                // Verificar si se deben actualizar coches para este modelo
                if (modeloDTO.getCoches() != null) {
                    for (CocheDTO cocheDTO : modeloDTO.getCoches()) {
                        // Crear un nuevo coche si no tiene ID o buscar el existente
                        Coche coche;
                        if (cocheDTO.getId() != null) {
                            Optional<Coche> optionalCoche = cocheRepository.findById(cocheDTO.getId());
                            if (optionalCoche.isPresent()) {
                                coche = optionalCoche.get();
                            } else {
                                coche = new Coche();
                                coche.setModelo(modelo);
                            }
                        } else {
                            coche = new Coche();
                            coche.setModelo(modelo);
                        }

                        // Actualizar los atributos obligatorios del coche
                        if (cocheDTO.getColor() != null && !cocheDTO.getColor().isEmpty()) {
                            coche.setColor(cocheDTO.getColor());
                        }
                        if (cocheDTO.getMatricula() != null && !cocheDTO.getMatricula().isEmpty()) {
                            coche.setMatricula(cocheDTO.getMatricula());
                        }

                        // Actualizar los atributos opcionales del coche
                        if (cocheDTO.getPrecio() != 0) {
                            coche.setPrecio(cocheDTO.getPrecio());
                        }
                        if (cocheDTO.getFechaFabricacion() != null) {
                            coche.setFechaFabricacion(cocheDTO.getFechaFabricacion());
                        }

                        // Guardar el coche actualizado
                        cocheRepository.save(coche);
                    }
                }
            }
        }

        return ResponseEntity.ok("Marca actualizada correctamente.");
    }

    // Actualizar información de marcas existentes
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarMarca(
            @PathVariable Long id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String sitioWeb,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String anyoFundacion) {

        Optional<Marca> optionalMarca = marcaRepository.findById(id);
        if (!optionalMarca.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Marca marca = optionalMarca.get();

        // Actualizar atributos de la marca si se proporcionan en la solicitud
        if (nombre != null) {
            marca.setNombre(nombre);
        }
        if (paisOrigen != null) {
            marca.setPaisOrigen(paisOrigen);
        }
        if (sitioWeb != null) {
            marca.setSitioWeb(sitioWeb);
        }
        if (telefono != null) {
            marca.setTelefono(telefono);
        }
        if (anyoFundacion != null) {
            marca.setAnyoFundacion(anyoFundacion);
        }

        // Guardar la marca actualizada en la base de datos
        marcaRepository.save(marca);

        return ResponseEntity.ok("Marca actualizada correctamente");
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
            @RequestParam(required = false) String anyoFundacion) {

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

    @GetMapping("/all")
    public List<Marca> getAllMarcasWithModelosAndCoches() {
        List<Marca> marcas = marcaRepository.findAllMarcas();
        for (Marca marca : marcas) {
            Hibernate.initialize(marca.getModelos());
            for (Modelo modelo : marca.getModelos()) {
                Hibernate.initialize(modelo.getCoches());
            }
        }
        return marcas;
    }

    @PostMapping("/marcas/search")
    public ResponseEntity<?> searchMarcas(@RequestBody SearchRequestDTO request) {
        try {
            // Validar el tamaño de la página
            int pageSize = request.getPage().getPageSize();
            if (pageSize <= 0) {
                return ResponseEntity.badRequest().body("El tamaño de la página debe ser mayor que cero.");
            }

            // Construir criterios de orden
            Sort sort = buildSortCriteria(request.getListOrderCriteria());

            // Construir criterios de búsqueda
            Specification<Marca> spec = buildSearchCriteria(request.getListSearchCriteria());

            // Paginación
            Pageable pageable = PageRequest.of(request.getPage().getPageIndex(), request.getPage().getPageSize(), sort);

            // Realizar la búsqueda paginada
            Page<Marca> marcasPage = marcaRepository.findAll(spec, pageable);

            // Mapear los resultados a MarcaDTO
            Page<MarcaDTO> marcasDTOPage = marcasPage.map(marca -> new MarcaDTO(
                    marca.getId(),
                    marca.getNombre(),
                    marca.getPaisOrigen(),
                    marca.getSitioWeb(),
                    marca.getTelefono(),
                    marca.getAnyoFundacion()
            ));

            return new ResponseEntity<>(marcasDTOPage, HttpStatus.OK);
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

    private Specification<Marca> buildSearchCriteria(List<SearchRequestDTO.SearchCriteriaDTO> listSearchCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchRequestDTO.SearchCriteriaDTO searchCriteria : listSearchCriteria) {
                switch (searchCriteria.getKey()) {
                    case "nombre", "sitioWeb", "telefono" -> {
                        if (searchCriteria.getOperation().equals("like")) {
                            predicates.add(cb.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%"));
                        } else if (searchCriteria.getOperation().equals("equal")) {
                            predicates.add(cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue()));
                        }
                    }
                    case "paisOrigen" -> {
                        if (searchCriteria.getOperation().equals("equal")) {
                            predicates.add(cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue()));
                        }
                    }
                    case "anyoFundacion" -> {
                        int year = Integer.parseInt(searchCriteria.getValue());
                        switch (searchCriteria.getOperation()) {
                            case "greater_than" ->
                                    predicates.add(cb.greaterThan(root.get(searchCriteria.getKey()), year));
                            case "equal" -> predicates.add(cb.equal(root.get(searchCriteria.getKey()), year));
                            case "lower_than" -> predicates.add(cb.lessThan(root.get(searchCriteria.getKey()), year));
                        }
                    }
                    // Agrega más casos según sea necesario para otras claves de búsqueda
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}