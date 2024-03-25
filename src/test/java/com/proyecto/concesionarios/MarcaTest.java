package com.proyecto.concesionarios;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarcaTest {

    private Marca marca;

    @Mock
    private List<Concesionario> concesionariosMock;

    @Mock
    private List<Modelo> modelosMock;

    @BeforeEach
    public void setUp() {
        marca = new Marca();
    }

    @Test
    public void testId() {
        Long id = 1L;
        marca.setId(id);
        assertEquals(id, marca.getId());
    }

    @Test
    public void testNombre() {
        String nombre = "Marca Test";
        marca.setNombre(nombre);
        assertEquals(nombre, marca.getNombre());
    }

    @Test
    public void testPaisOrigen() {
        String paisOrigen = "País de Origen";
        marca.setPaisOrigen(paisOrigen);
        assertEquals(paisOrigen, marca.getPaisOrigen());
    }

    @Test
    public void testSitioWeb() {
        String sitioWeb = "https://www.marca.com";
        marca.setSitioWeb(sitioWeb);
        assertEquals(sitioWeb, marca.getSitioWeb());
    }

    @Test
    public void testTelefono() {
        String telefono = "123456789";
        marca.setTelefono(telefono);
        assertEquals(telefono, marca.getTelefono());
    }

    @Test
    public void testAnyoFundacion() {
        String anyoFundacion = "2000";
        marca.setAnyoFundacion(anyoFundacion);
        assertEquals(anyoFundacion, marca.getAnyoFundacion());
    }

    // Prueba para la relación con los concesionarios
    @Test
    public void testConcesionarios() {

        Concesionario concesionario1 = new Concesionario();
        Concesionario concesionario2 = new Concesionario();

        concesionariosMock.add(concesionario1);
        concesionariosMock.add(concesionario2);

        // Configurar el comportamiento del mock para simular la relación con concesionarios
        when(concesionariosMock.size()).thenReturn(2);
        when(concesionariosMock.get(0)).thenReturn(concesionario1);
        when(concesionariosMock.get(1)).thenReturn(concesionario2);

        // Establecer la lista mock de concesionarios en la marca
        marca.setConcesionarios(concesionariosMock);

        // Verificar que la relación con concesionarios se haya establecido correctamente
        assertEquals(2, marca.getConcesionarios().size());
        assertEquals(concesionario1, marca.getConcesionarios().get(0));
        assertEquals(concesionario2, marca.getConcesionarios().get(1));
    }

    @Test
    public void testModelos() {

        Modelo modelo1 = new Modelo();
        Modelo modelo2 = new Modelo();

        modelosMock.add(modelo1);
        modelosMock.add(modelo2);

        // Configurar el comportamiento del mock para simular la relación con modelos
        when(modelosMock.size()).thenReturn(2);
        when(modelosMock.get(0)).thenReturn(modelo1);
        when(modelosMock.get(1)).thenReturn(modelo2);

        // Establecer la lista mock de modelos en la marca
        marca.setModelos(modelosMock);

        // Verificar que la relación con modelos se haya establecido correctamente
        assertEquals(2, marca.getModelos().size());
        assertEquals(modelo1, marca.getModelos().get(0));
        assertEquals(modelo2, marca.getModelos().get(1));
    }
}
