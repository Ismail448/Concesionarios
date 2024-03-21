package com.proyecto.concesionarios.controller;

import com.proyecto.concesionarios.dto.CocheDTO;
import com.proyecto.concesionarios.dto.ConcesionarioDTO;
import com.proyecto.concesionarios.dto.MarcaDTO;
import com.proyecto.concesionarios.dto.ModeloDTO;
import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.CocheRepository;
import com.proyecto.concesionarios.repository.ConcesionarioRepository;
import com.proyecto.concesionarios.repository.MarcaRepository;
import com.proyecto.concesionarios.repository.ModeloRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RestController
@RequestMapping("/concesionario")
public class ConcesionarioRestController {

    @Autowired
    private ConcesionarioRepository concesionarioRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private CocheRepository cocheRepository;

    //Registrar nuevos concesionarios y posibilidad de registrar tambien marcas, modelos y coches
    @PostMapping("ConcesionarioJson")
    public ResponseEntity<String> registrarCocnesionarioJson(@RequestBody ConcesionarioDTO concesionarioDTO) {
        //Validar que al menos dos atributos sean obligatorios
        if (concesionarioDTO.getNombre() == null || concesionarioDTO.getNombre().isEmpty() ||
                concesionarioDTO.getDireccion() == null || concesionarioDTO.getDireccion().isEmpty()) {
            return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para el concesionario.");
        }

        Concesionario concesionario = new Concesionario();
        concesionario.setNombre(concesionarioDTO.getNombre());
        concesionario.setDireccion(concesionarioDTO.getDireccion());
        concesionario.setTelefono(concesionarioDTO.getTelefono());
        concesionario.setEmail(concesionarioDTO.getEmail());
        concesionario.setSitioWeb(concesionarioDTO.getSitioWeb());

        // Crear y asociar marcas al concesionario
        List<Marca> marcas = new ArrayList<>();
        if (concesionarioDTO.getMarcas() != null) {
            for (MarcaDTO marcaDTO : concesionarioDTO.getMarcas()) {
                // Validar que al menos dos atributos sean obligatorios para la marca
                if (marcaDTO.getNombre() == null || marcaDTO.getNombre().isEmpty() ||
                        marcaDTO.getPaisOrigen() == null || marcaDTO.getPaisOrigen().isEmpty()) {
                    return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para cada marca.");
                }
                Marca marca = new Marca();
                marca.setNombre(marcaDTO.getNombre());
                marca.setPaisOrigen(marcaDTO.getPaisOrigen());
                marca.setSitioWeb(marcaDTO.getSitioWeb());
                marca.setTelefono(marcaDTO.getTelefono());
                marca.setAnyoFundacion(marcaDTO.getAnyoFundacion());
                marcaRepository.save(marca);
                marcas.add(marca);

                // Verificar si se debe registrar un modelo para esta marca
                if (marcaDTO.getModelo() != null) {
                    // Validar que al menos dos atributos sean obligatorios para el modelo
                    ModeloDTO modeloDTO = marcaDTO.getModelo();
                    if (modeloDTO.getNombre() == null || modeloDTO.getNombre().isEmpty() ||
                            modeloDTO.getTipoCoche() == null || modeloDTO.getTipoCoche().isEmpty()) {
                        return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para el modelo.");
                    }
                    Modelo modelo = new Modelo();
                    modelo.setNombre(modeloDTO.getNombre());
                    modelo.setTipoCoche(modeloDTO.getTipoCoche());
                    modelo.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());
                    modelo.setMarca(marca);
                    modeloRepository.save(modelo);

                    // Verificar si se debe registrar un coche para este modelo
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
        }
        concesionario.setMarcas(marcas);

        // Guardar el concesionario y obtener su ID
        Concesionario savedConcesionario = concesionarioRepository.save(concesionario);
        Long concesionarioId = savedConcesionario.getId();

        return ResponseEntity.ok("Concesionario registrado correctamente con ID: " + concesionarioId);
    }

    //Registrar nuevos concesionarios y posibilidad de registrar tambien marcas, modelos y coches
    @PostMapping
    public ResponseEntity<String> registrarConcesionario(
            @RequestParam String nombre,
            @RequestParam String direccion,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String sitioWeb,
            @RequestParam(required = false) List<Long> marcasIds) {

        Concesionario nuevoConcesionario = new Concesionario();
        nuevoConcesionario.setNombre(nombre);
        nuevoConcesionario.setDireccion(direccion);
        nuevoConcesionario.setTelefono(telefono);
        nuevoConcesionario.setEmail(email);
        nuevoConcesionario.setSitioWeb(sitioWeb);

        if (marcasIds != null && !marcasIds.isEmpty()) {
            List<Marca> marcas = marcaRepository.findAllById(marcasIds);
            nuevoConcesionario.setMarcas(marcas);
        }

        concesionarioRepository.save(nuevoConcesionario);

        return ResponseEntity.ok("Concesionario registrado correctamente");
    }

    //Actualizar información de concesionarios existentes
    @PutMapping("ConcesionarioJson/{id}")
    public ResponseEntity<String> actualizarConcesionarioJson(@PathVariable Long id, @RequestBody ConcesionarioDTO concesionarioDTO) {
        Optional<Concesionario> optionalConcesionario = concesionarioRepository.findById(id);
        if (!optionalConcesionario.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Validar que al menos dos atributos sean obligatorios
        if (concesionarioDTO.getNombre() == null || concesionarioDTO.getNombre().isEmpty() ||
                concesionarioDTO.getDireccion() == null || concesionarioDTO.getDireccion().isEmpty()) {
            return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para el concesionario.");
        }

        Concesionario concesionario = optionalConcesionario.get();
        concesionario.setNombre(concesionarioDTO.getNombre());
        concesionario.setDireccion(concesionarioDTO.getDireccion());
        concesionario.setTelefono(concesionarioDTO.getTelefono());
        concesionario.setEmail(concesionarioDTO.getEmail());
        concesionario.setSitioWeb(concesionarioDTO.getSitioWeb());

        // Actualizar y asociar marcas al concesionario
        List<Marca> marcas = new ArrayList<>();
        if (concesionarioDTO.getMarcas() != null) {
            for (MarcaDTO marcaDTO : concesionarioDTO.getMarcas()) {
                // Validar que al menos dos atributos sean obligatorios para la marca
                if (marcaDTO.getNombre() == null || marcaDTO.getNombre().isEmpty() ||
                        marcaDTO.getPaisOrigen() == null || marcaDTO.getPaisOrigen().isEmpty()) {
                    return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para cada marca.");
                }
                Marca marca = new Marca();
                marca.setNombre(marcaDTO.getNombre());
                marca.setPaisOrigen(marcaDTO.getPaisOrigen());
                marca.setSitioWeb(marcaDTO.getSitioWeb());
                marca.setTelefono(marcaDTO.getTelefono());
                marca.setAnyoFundacion(marcaDTO.getAnyoFundacion());
                marcaRepository.save(marca);
                marcas.add(marca);

                // Verificar si se debe actualizar un modelo para esta marca
                if (marcaDTO.getModelo() != null) {
                    // Validar que al menos dos atributos sean obligatorios para el modelo
                    ModeloDTO modeloDTO = marcaDTO.getModelo();
                    if (modeloDTO.getNombre() == null || modeloDTO.getNombre().isEmpty() ||
                            modeloDTO.getTipoCoche() == null || modeloDTO.getTipoCoche().isEmpty()) {
                        return ResponseEntity.badRequest().body("Se requieren al menos dos atributos obligatorios para el modelo.");
                    }
                    Modelo modelo = new Modelo();
                    modelo.setNombre(modeloDTO.getNombre());
                    modelo.setTipoCoche(modeloDTO.getTipoCoche());
                    modelo.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());
                    modelo.setMarca(marca);
                    modeloRepository.save(modelo);

                    // Verificar si se debe actualizar un coche para este modelo
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
        }
        concesionario.setMarcas(marcas);

        // Guardar el concesionario actualizado
        concesionarioRepository.save(concesionario);

        return ResponseEntity.ok("Concesionario actualizado correctamente.");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> actualizarConcesionario(
            @PathVariable Long id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sitioWeb,
            @RequestParam(required = false) List<Long> marcasIds) {

        Optional<Concesionario> optionalConcesionario = concesionarioRepository.findById(id);
        if (!optionalConcesionario.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Concesionario concesionario = optionalConcesionario.get();

        // Actualizar atributos del concesionario
        if (nombre != null) {
            concesionario.setNombre(nombre);
        }
        if (direccion != null) {
            concesionario.setDireccion(direccion);
        }
        if (telefono != null) {
            concesionario.setTelefono(telefono);
        }
        if (email != null) {
            concesionario.setEmail(email);
        }
        if (sitioWeb != null) {
            concesionario.setSitioWeb(sitioWeb);
        }

        // Actualizar relaciones con marcas si se proporcionan nuevos IDs de marcas
        if (marcasIds != null && !marcasIds.isEmpty()) {
            List<Marca> marcas = marcaRepository.findAllById(marcasIds);
            concesionario.setMarcas(marcas);
        }

        concesionarioRepository.save(concesionario);

        return ResponseEntity.ok("Concesionario actualizado correctamente");
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
