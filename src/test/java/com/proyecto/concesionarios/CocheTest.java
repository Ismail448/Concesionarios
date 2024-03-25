package com.proyecto.concesionarios;

import com.proyecto.concesionarios.entity.Coche;
import com.proyecto.concesionarios.entity.Modelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CocheTest {

    private Coche coche;

    @Mock
    private Modelo modeloMock;

    @BeforeEach
    public void setUp() {
        coche = new Coche();
    }

    @Test
    public void testId() {
        Long id = 1L;
        coche.setId(id);
        assertEquals(id, coche.getId());
    }

    @Test
    public void testColor() {
        String color = "Rojo";
        coche.setColor(color);
        assertEquals(color, coche.getColor());
    }

    @Test
    public void testMatricula() {
        String matricula = "ABC1234";
        coche.setMatricula(matricula);
        assertEquals(matricula, coche.getMatricula());
    }

    @Test
    public void testPrecio() {
        Float precio = 15000.0f;
        coche.setPrecio(precio);
        assertEquals(precio, coche.getPrecio());
    }

    @Test
    public void testFechaFabricacion() {
        LocalDate fechaFabricacion = LocalDate.of(2022, 3, 15);
        coche.setFechaFabricacion(fechaFabricacion);
        assertEquals(fechaFabricacion, coche.getFechaFabricacion());
    }

    @Test
    public void testModelo() {
        coche.setModelo(modeloMock);
        assertEquals(modeloMock, coche.getModelo());
    }
}
