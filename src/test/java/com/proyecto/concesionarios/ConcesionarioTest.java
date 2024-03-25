package com.proyecto.concesionarios;

import com.proyecto.concesionarios.entity.Concesionario;
import com.proyecto.concesionarios.entity.Marca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcesionarioTest {

    private Concesionario concesionario;

    @Mock
    private List<Marca> marcasMock;

    @BeforeEach
    public void setUp() {
        concesionario = new Concesionario();
    }

    @Test
    public void testId() {
        Long id = 1L;
        concesionario.setId(id);
        assertEquals(id, concesionario.getId());
    }

    @Test
    public void testNombre() {
        String nombre = "Concesionario Test";
        concesionario.setNombre(nombre);
        assertEquals(nombre, concesionario.getNombre());
    }

    @Test
    public void testDireccion() {
        String direccion = "Calle de ejemplo, 123";
        concesionario.setDireccion(direccion);
        assertEquals(direccion, concesionario.getDireccion());
    }

    @Test
    public void testTelefono() {
        String telefono = "123456789";
        concesionario.setTelefono(telefono);
        assertEquals(telefono, concesionario.getTelefono());
    }

    @Test
    public void testEmail() {
        String email = "concesionario@test.com";
        concesionario.setEmail(email);
        assertEquals(email, concesionario.getEmail());
    }

    @Test
    public void testSitioWeb() {
        String sitioWeb = "https://www.concesionario.com";
        concesionario.setSitioWeb(sitioWeb);
        assertEquals(sitioWeb, concesionario.getSitioWeb());
    }

    @Test
    public void testMarcas() {
        // Crear algunas marcas de ejemplo
        Marca marca1 = new Marca();
        marca1.setId(1L);
        marca1.setNombre("Marca 1");

        Marca marca2 = new Marca();
        marca2.setId(2L);
        marca2.setNombre("Marca 2");

        List<Marca> marcas = new ArrayList<>();
        marcas.add(marca1);
        marcas.add(marca2);

        // Configurar el comportamiento del mock para simular las marcas asociadas al concesionario
        when(marcasMock.size()).thenReturn(marcas.size());
        when(marcasMock.get(0)).thenReturn(marca1);
        when(marcasMock.get(1)).thenReturn(marca2);

        // Establecer las marcas en el concesionario
        concesionario.setMarcas(marcasMock);

        // Verificar que las marcas se han establecido correctamente en el concesionario
        assertEquals(marcas.size(), concesionario.getMarcas().size());
        assertEquals(marca1, concesionario.getMarcas().get(0));
        assertEquals(marca2, concesionario.getMarcas().get(1));
    }
}
