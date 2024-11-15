package com.example.gomesrodris.archburgers.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StatusPedidoTest {

    @Test
    void testValoresEsperados() {
        // Verifica se todos os valores esperados estão presentes
        assertEquals(6, StatusPedido.values().length);
    }

    @Test
    void testComparacoes() {
        assertEquals(StatusPedido.PAGAMENTO, StatusPedido.PAGAMENTO);
        assertNotEquals(StatusPedido.PAGAMENTO, StatusPedido.RECEBIDO);
    }
}