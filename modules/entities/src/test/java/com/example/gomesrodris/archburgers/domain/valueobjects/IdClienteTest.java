package com.example.gomesrodris.archburgers.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class IdClienteTest {

    @Test
    void validConstructor() {
        int codigo = 123;
        IdCliente id = new IdCliente(123);
        assertEquals(id.id(), codigo);
    }

    @Test
    void invalidConstructor() {
        int codigo = 999;
        IdCliente id = new IdCliente(123);
        assertNotEquals(id.id(), codigo);
    }

    @Test
    void testConstrutorInteger() {
        Integer id = 20;
        IdCliente clienteId = new IdCliente(id);

        assertEquals(id.intValue(), clienteId.id());
    }

    @Test
    void testEquals() {
        int id = 30;
        IdCliente clienteId1 = new IdCliente(id);
        IdCliente clienteId2 = new IdCliente(id);

        assertEquals(clienteId1, clienteId2);
        assertEquals(clienteId1.hashCode(), clienteId2.hashCode());
    }

    @Test
    void testNotEquals() {
        IdCliente clienteId1 = new IdCliente(40);
        IdCliente clienteId2 = new IdCliente(50);

        assertNotEquals(clienteId1, clienteId2);
    }
}