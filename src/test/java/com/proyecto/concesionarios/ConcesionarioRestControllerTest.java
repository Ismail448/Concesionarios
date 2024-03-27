package com.proyecto.concesionarios;

import com.proyecto.concesionarios.controller.ConcesionarioRestController;
import com.proyecto.concesionarios.dto.ConcesionarioDTO;
import com.proyecto.concesionarios.dto.SearchRequestDTO;
import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.repository.ConcesionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ConcesionarioRestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ConcesionarioRestController concesionarioRestController;

    @Mock
    private ConcesionarioRepository concesionarioRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(concesionarioRestController).build();
    }

    @Test
    public void testRegistrarConcesionario() {
        //ConcesionarioDTO
        ConcesionarioDTO concesionarioDTO = new ConcesionarioDTO(1L,"nombre","direccion","telefono","email","sitioWeb");
        concesionarioDTO.setNombre("Nombre Concesionario");
        concesionarioDTO.setDireccion("Dirección Concesionario");

        //Concesionario
        Concesionario concesionario = new Concesionario();
        concesionario.setId(1L);
        concesionario.setNombre(concesionarioDTO.getNombre());
        concesionario.setDireccion(concesionarioDTO.getDireccion());

        when(concesionarioRepository.save(any(Concesionario.class))).thenReturn(concesionario);

        ResponseEntity<String> response = concesionarioRestController.registrarConcesionario(
                concesionarioDTO.getNombre(),
                concesionarioDTO.getDireccion(),
                null,
                null,
                null,
                null);

        assertEquals("Concesionario registrado correctamente", response.getBody());
    }
    @Test
    void testActualizarConcesionario() throws Exception {
        // Simular un concesionario existente
        Concesionario concesionarioExistente = new Concesionario();
        concesionarioExistente.setId(1L);
        when(concesionarioRepository.findById(1L)).thenReturn(Optional.of(concesionarioExistente));

        // Simular la actualización de un concesionario
        mockMvc.perform(put("/concesionario/1")
                        .param("nombre", "NuevoNombre")
                        .param("direccion", "NuevaDirección")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Concesionario actualizado correctamente"));

        // Verificar que se llame al método save del repositorio
        verify(concesionarioRepository, times(1)).save(any(Concesionario.class));
    }

    @Test
    void testEliminarConcesionario() throws Exception {
        // Simular un concesionario existente
        when(concesionarioRepository.existsById(1L)).thenReturn(true);

        // Simular la eliminación de un concesionario
        mockMvc.perform(delete("/concesionario/1"))
                .andExpect(status().isNoContent());

        // Verificar que se llame al método deleteById del repositorio
        verify(concesionarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testBuscarConcesionarios() throws Exception {
        // Simular una lista de concesionarios
        List<Concesionario> concesionarios = new ArrayList<>();
        concesionarios.add(new Concesionario());
        when(concesionarioRepository.findByNombreContainingAndDireccionContainingAndTelefonoContainingAndEmailContainingAndSitioWebContaining(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(concesionarios);

        // Simular una solicitud de búsqueda de concesionarios con parámetros de consulta
        mockMvc.perform(get("/concesionario")
                        .param("nombre", "ejemploNombre")
                        .param("direccion", "ejemploDireccion")
                        .param("telefono", "ejemploTelefono")
                        .param("email", "ejemploEmail")
                        .param("sitioWeb", "ejemploSitioWeb"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Verificar que se llame al método findByNombreContainingAndDireccionContainingAndTelefonoContainingAndEmailContainingAndSitioWebContaining del repositorio
        verify(concesionarioRepository, times(1)).findByNombreContainingAndDireccionContainingAndTelefonoContainingAndEmailContainingAndSitioWebContaining(anyString(), anyString(), anyString(), anyString(), anyString());
    }


    @Test
    void testBuscarConcesionariosPaginados() {
        // Crear una lista de concesionarios simulada
        List<Concesionario> concesionarios = new ArrayList<>();
        concesionarios.add(new Concesionario());

        // Crear una página de concesionarios simulada
        Page<Concesionario> pageOfConcesionarios = new PageImpl<>(concesionarios);

        // Simular el comportamiento del repositorio para devolver la página de concesionarios simulada
        when(concesionarioRepository.findAll(any(Pageable.class))).thenReturn(pageOfConcesionarios);

        // Llamar al método del controlador para buscar concesionarios paginados
        ResponseEntity<Page<Concesionario>> result = concesionarioRestController.buscarConcesionariosPaginados(0, 10);

        // Verificar que la página devuelta coincida con la página simulada
        assertEquals(pageOfConcesionarios, result.getBody());
    }


    @Test
    void testGetAllConcesionariosWithMarcasModelosAndCoches() throws Exception {
        // Simular una lista de concesionarios con marcas, modelos y coches asociados
        List<Concesionario> concesionarios = new ArrayList<>();
        Concesionario concesionario = new Concesionario();
        concesionario.setMarcas(new ArrayList<>()); // Agrega marcas, modelos y coches según sea necesario
        concesionarios.add(concesionario);
        when(concesionarioRepository.findAllWithMarcas()).thenReturn(concesionarios);

        // Simular una solicitud para obtener todos los concesionarios con marcas, modelos y coches asociados
        mockMvc.perform(get("/concesionario/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Verificar que se llame al método findAllWithMarcas del repositorio
        verify(concesionarioRepository, times(1)).findAllWithMarcas();
    }

    @Test
    void testSearchConcesionarios() {
        // Mock de datos de solicitud
        SearchRequestDTO.PageDTO pageDTO = new SearchRequestDTO.PageDTO(0, 10);
        SearchRequestDTO request = new SearchRequestDTO(
                Collections.emptyList(),
                Collections.emptyList(),
                pageDTO
        );

        // Mock de datos de respuesta
        Concesionario concesionario = new Concesionario();
        concesionario.setId(1L);
        concesionario.setNombre("Concesionario de Prueba");
        concesionario.setDireccion("Dirección de Prueba");
        concesionario.setTelefono("123456789");
        concesionario.setEmail("test@example.com");
        concesionario.setSitioWeb("www.example.com");
        PageImpl<Concesionario> page = new PageImpl<>(Collections.singletonList(concesionario));

        // Configuración del mock del repositorio
        when(concesionarioRepository.findAll((Specification<Concesionario>) any(), any()))
                .thenReturn(page);

        // Ejecutar el método del controlador
        ResponseEntity<?> response = concesionarioRestController.searchConcesionarios(request);

        // Verificar la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar el contenido de la respuesta
        PageImpl<ConcesionarioDTO> expectedPage = new PageImpl<>(
                Collections.singletonList(new ConcesionarioDTO(
                        1L,
                        "Concesionario de Prueba",
                        "Dirección de Prueba",
                        "123456789",
                        "test@example.com",
                        "www.example.com"
                ))
        );
        assertEquals(expectedPage, response.getBody());
    }
}


