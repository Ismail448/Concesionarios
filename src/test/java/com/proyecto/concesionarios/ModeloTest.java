package com.proyecto.concesionarios;

import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Marca;
import com.proyecto.concesionarios.entity.Modelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ModeloTest {

    private Modelo modelo;

    @Mock
    private Marca marcaMock;

    @Mock
    private List<Coche> cochesMock;

    @BeforeEach
    public void setUp() {
        modelo = new Modelo();
    }

    @Test
    public void testId() {
        Long id = 1L;
        modelo.setId(id);
        assertEquals(id, modelo.getId());
    }

    @Test
    public void testNombre() {
        String nombre = "Modelo Test";
        modelo.setNombre(nombre);
        assertEquals(nombre, modelo.getNombre());
    }

    @Test
    public void testTipoCoche() {
        String tipoCoche = "Sedán";
        modelo.setTipoCoche(tipoCoche);
        assertEquals(tipoCoche, modelo.getTipoCoche());
    }

    @Test
    public void testAnyoLanzamiento() {
        Integer anyoLanzamiento = 2022;
        modelo.setAnyoLanzamiento(anyoLanzamiento);
        assertEquals(anyoLanzamiento, modelo.getAnyoLanzamiento());
    }

    @Test
    public void testMarca() {
        modelo.setMarca(marcaMock);
        assertEquals(marcaMock, modelo.getMarca());
    }

    @Test
    public void testCoches() {
        modelo.setCoches(cochesMock);
        assertEquals(cochesMock, modelo.getCoches());
    }
}
