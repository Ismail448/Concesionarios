package com.proyecto.concesionarios;

import com.proyecto.concesionarios.controller.MarcaRestController;
import com.proyecto.concesionarios.dto.*;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.MarcaRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MarcaRestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MarcaRestController marcaRestController;

    @Mock
    private MarcaRepository marcaRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(marcaRestController).build();
    }

    @Test
    public void testRegistrarMarcaJson() {
        // Arrange
        MarcaDTO marcaDTO = new MarcaDTO(1L,"nombre","pais","sitioWeb", "+34655433212","2000");
        marcaDTO.setNombre("Toyota");
        marcaDTO.setPaisOrigen("Japón");

        Marca marcaMock = new Marca();
        marcaMock.setNombre(marcaDTO.getNombre());
        marcaMock.setPaisOrigen(marcaDTO.getPaisOrigen());

        when(marcaRepository.save(any(Marca.class))).thenReturn(marcaMock);

        // Act
        ResponseEntity<String> response = marcaRestController.registrarMarcaJson(marcaDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Marca registrada correctamente.", response.getBody());
    }

    @Test
    void testActualizarMarcaJson() {
        // Arrange
        Long id = 1L;
        MarcaDTO marcaDTO = new MarcaDTO(1L,"nombre","pais","sitioWeb", "+34655433212","2000");
        marcaDTO.setNombre("Toyota");
        marcaDTO.setPaisOrigen("Japón");

        Marca marcaExistente = new Marca();
        marcaExistente.setId(id);
        marcaExistente.setNombre("Toyota");
        marcaExistente.setPaisOrigen("Japón");

        when(marcaRepository.findById(id)).thenReturn(Optional.of(marcaExistente));

        // Act
        ResponseEntity<String> response = marcaRestController.actualizarMarcaJson(id, marcaDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Marca actualizada correctamente.", response.getBody());
    }

    @Test
    void testEliminarMarca() {
        // Arrange
        Long id = 1L;
        when(marcaRepository.existsById(id)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = marcaRestController.eliminarMarca(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(marcaRepository, times(1)).deleteById(id);
    }

    @Test
    void testBuscarMarcas() throws Exception {
        // Arrange
        List<Marca> marcas = new ArrayList<>();
        marcas.add(new Marca());
        when(marcaRepository.findByNombreContainingAndPaisOrigenContainingAndSitioWebContainingAndTelefonoContainingAndAnyoFundacionContaining(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(marcas);

        // Simular una solicitud de búsqueda de concesionarios con parámetros de consulta
        mockMvc.perform(get("/marcas")
                        .param("nombre", "ejemploNombre")
                        .param("paisOrigen", "ejemploPaisOrigen")
                        .param("sitioWeb", "ejemploSitioWeb")
                        .param("telefono", "ejemploTelefono")
                        .param("anyoFundacion", "2022")) // Por ejemplo, aquí puedes proporcionar un año de fundación válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Verificar que se llame al método findByNombreContainingAndPaisOrigenContainingAndSitioWebContainingAndTelefonoContainingAndAnyoFundacionContaining del repositorio
        verify(marcaRepository, times(1)).findByNombreContainingAndPaisOrigenContainingAndSitioWebContainingAndTelefonoContainingAndAnyoFundacionContaining(anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void testBuscarMarcasPaginadas() {
        // Arrange
        int page = 0;
        int size = 10;
        List<Marca> marcas = new ArrayList<>();
        marcas.add(new Marca());
        Page<Marca> pageOfMarcas = new PageImpl<>(marcas);
        when(marcaRepository.findAll(any(Pageable.class))).thenReturn(pageOfMarcas);

        // Act
        ResponseEntity<Page<Marca>> response = marcaRestController.buscarMarcasPaginadas(page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageOfMarcas, response.getBody());
    }

    @Test
    void testGetAllMarcasWithModelosAndCoches() {
        // Arrange
        List<Marca> marcas = new ArrayList<>();
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Toyota");
        marca.setPaisOrigen("Japón");
        marca.setModelos(Collections.singletonList(new Modelo()));
        marcas.add(marca);
        when(marcaRepository.findAllMarcas()).thenReturn(marcas);

        // Act
        List<Marca> result = marcaRestController.getAllMarcasWithModelosAndCoches();

        // Assert
        assertEquals(marcas, result);
    }

    @Test
    void testSearchMarcas() {
        // Mock de datos de solicitud
        SearchRequestDTO.PageDTO pageDTO = new SearchRequestDTO.PageDTO(0, 10);
        SearchRequestDTO request = new SearchRequestDTO(
                Collections.emptyList(),
                Collections.emptyList(),
                pageDTO
        );

        // Mock de datos de respuesta
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Nombre de Prueba");
        marca.setPaisOrigen("Pais de Prueba");
        marca.setSitioWeb("www.example.com");
        marca.setTelefono("+34233232399");
        marca.setAnyoFundacion("2000");
        PageImpl<Marca> page = new PageImpl<>(Collections.singletonList(marca));

        // Configuración del mock del repositorio
        when(marcaRepository.findAll((Specification<Marca>) any(), any()))
                .thenReturn(page);

        // Ejecutar el método del controlador
        ResponseEntity<?> response = marcaRestController.searchMarcas(request);

        // Verificar la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar el contenido de la respuesta
        PageImpl<MarcaDTO> responseBody = (PageImpl<MarcaDTO>) response.getBody();
        assertNotNull(responseBody);

        List<MarcaDTO> expectedContent = Collections.singletonList(new MarcaDTO(
                1L,
                "Nombre de Prueba",
                "Pais de Prueba",
                "www.example.com",
                "+34233232399",
                "2000"
        ));
        assertEquals(expectedContent, responseBody.getContent());
    }
}
