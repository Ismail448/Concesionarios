package com.proyecto.concesionarios.service;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.repository.ConcesionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConcesionarioService {

    @Autowired
    ConcesionarioRepository concesionarioRepository;

    public Concesionario save(Concesionario concesionario) {
        return concesionarioRepository.save(concesionario);
    }

    // Método para actualizar información de concesionarios existentes
    public Concesionario updateConcesionario(Long id, Concesionario newConcesionario) {
        Optional<Concesionario> optionalConcesionario = concesionarioRepository.findById(id);
        if (optionalConcesionario.isPresent()) {
            Concesionario concesionario = optionalConcesionario.get();
            concesionario.setNombre(newConcesionario.getNombre());
            concesionario.setDireccion(newConcesionario.getDireccion());
            // Continuar con los demás campos que se deseen actualizar
            return concesionarioRepository.save(concesionario);
        } else {
            // Manejo de error si el concesionario no se encuentra
            return null;
        }
    }

    // Método para eliminar concesionarios
    public void deleteConcesionario(Long id) {
        concesionarioRepository.deleteById(id);
    }

    // Método para consultar concesionarios filtrando por atributos
    public List<Concesionario> buscarConcesionarios(String nombre, String direccion, String telefono, String email, String sitioWeb) {
        return concesionarioRepository.findByNombreContainingAndDireccionContainingAndTelefonoContainingAndEmailContainingAndSitioWebContaining(nombre, direccion, telefono, email, sitioWeb);
    }

    // Método para consultar concesionarios de forma paginada (RF1.5)
    public Page<Concesionario> buscarConcesionariosPaginados(Pageable pageable) {
        return concesionarioRepository.findAll(pageable);
    }
}
