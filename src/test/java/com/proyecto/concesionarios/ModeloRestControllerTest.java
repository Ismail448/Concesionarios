package com.proyecto.concesionarios;


import com.proyecto.concesionarios.controller.ModeloRestController;
import com.proyecto.concesionarios.dto.ModeloDTO;
import com.proyecto.concesionarios.dto.SearchRequestDTO;
import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import com.proyecto.concesionarios.repository.ModeloRepository;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ModeloRestControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ModeloRepository modeloRepository;

    @InjectMocks
    private ModeloRestController modeloRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(modeloRestController).build();
    }

    @Test
    public void testRegistrarModeloJson() {

        ModeloDTO modeloDTO = new ModeloDTO(1L,"nombre","tipoCoche",2000,null);
        modeloDTO.setNombre("Modelo1");
        modeloDTO.setTipoCoche("Tipo1");
        modeloDTO.setAnyoLanzamiento(2022);

        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setNombre(modeloDTO.getNombre());
        modelo.setTipoCoche(modeloDTO.getTipoCoche());
        modelo.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());

        when(modeloRepository.save(any(Modelo.class))).thenReturn(modelo);

        ResponseEntity<Object> responseEntity = modeloRestController.registrarModeloJson(modeloDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testActualizarModeloJson() {
        Long modeloId = 1L;
        ModeloDTO modeloDTO = new ModeloDTO(1L,"nombre","tipoCoche",2000,null);
        modeloDTO.setNombre("Modelo1");
        modeloDTO.setTipoCoche("Tipo1");
        modeloDTO.setAnyoLanzamiento(2022);

        Modelo modelo = new Modelo();
        modelo.setId(modeloId);
        modelo.setNombre(modeloDTO.getNombre());
        modelo.setTipoCoche(modeloDTO.getTipoCoche());
        modelo.setAnyoLanzamiento(modeloDTO.getAnyoLanzamiento());

        when(modeloRepository.findById(modeloId)).thenReturn(Optional.of(modelo));

        ResponseEntity<Object> responseEntity = modeloRestController.actualizarModeloJson(modeloId, modeloDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Modelo modificado correctamente.", responseEntity.getBody());
    }

    @Test
    public void testEliminarModelo() {
        Long modeloId = 1L;

        when(modeloRepository.existsById(modeloId)).thenReturn(true);

        ResponseEntity<String> responseEntity = modeloRestController.eliminarModelo(modeloId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Modelo con ID 1 eliminado correctamente", responseEntity.getBody());
    }

    @Test
    public void testBuscarModelo() throws Exception {
        List<Modelo> modelos = new ArrayList<>();
        modelos.add(new Modelo());
        when(modeloRepository.findByNombreContainingAndTipoCocheContainingAndAnyoLanzamiento(anyString(), anyString(), anyInt()))
                .thenReturn(modelos);

        mockMvc.perform(get("/modelos")
                        .param("nombre", "ejemploNombre")
                        .param("tipoCoche", "ejemploTipoCoche")
                        .param("anyoLanzamiento", "2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(modeloRepository, times(1)).findByNombreContainingAndTipoCocheContainingAndAnyoLanzamiento(anyString(), anyString(), anyInt());

    }

    @Test
    public void testBuscarModelosPaginados() {

        int page = 0;
        int size = 10;
        List<Modelo> modelos = new ArrayList<>();
        modelos.add(new Modelo());
        Page<Modelo> pageOfModelos = new PageImpl<>(modelos);
        when(modeloRepository.findAll(any(Pageable.class))).thenReturn(pageOfModelos);

        ResponseEntity<Page<Modelo>> response = modeloRestController.buscarModelosPaginados(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageOfModelos, response.getBody());
    }

    @Test
    public void getAllModelosWithCoches(){
        List<Modelo> modelos = new ArrayList<>();
        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setNombre("Yaris");
        modelo.setTipoCoche("suv");
        modelo.setAnyoLanzamiento(2020);
        modelo.setCoches(Collections.singletonList(new Coche()));
        modelos.add(modelo);
        when(modeloRepository.findAllModelos()).thenReturn(modelos);

        List<Modelo> result = modeloRestController.getAllModelosWithCoches();

        assertEquals(modelos, result);
    }

    @Test
    void testSearchModelos() {
        // Mock de datos de solicitud
        SearchRequestDTO.PageDTO pageDTO = new SearchRequestDTO.PageDTO(0, 10);
        SearchRequestDTO request = new SearchRequestDTO(
                Collections.emptyList(),
                Collections.emptyList(),
                pageDTO
        );

        // Mock de datos de respuesta
        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setNombre("Nombre de Prueba");
        modelo.setTipoCoche("Tipo de coche");
        modelo.setAnyoLanzamiento(2020);

        Marca marca = new Marca();
        marca.setId(1L);
        modelo.setMarca(marca);

        PageImpl<Modelo> page = new PageImpl<>(Collections.singletonList(modelo));

        // Configuración del mock del repositorio
        when(modeloRepository.findAll((Specification<Modelo>) any(), any()))
                .thenReturn(page);

        // Ejecutar el método del controlador
        ResponseEntity<?> response = modeloRestController.searchModelos(request);

        // Verificar la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar el contenido de la respuesta
        PageImpl<ModeloDTO> responseBody = (PageImpl<ModeloDTO>) response.getBody();
        assertNotNull(responseBody);

        List<ModeloDTO> expectedContent = Collections.singletonList(new ModeloDTO(
                1L,
                "Nombre de Prueba",
                "Tipo de coche",
                2020,
                1L
        ));
        assertEquals(expectedContent, responseBody.getContent());
    }


}
