package com.example.gomesrodris.archburgers.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StatusPagamentoTest {

    @Test
    void testValoresEsperados() {
        // Verifica se todos os valores esperados estão presentes
        assertEquals(2, StatusPagamento.values().length);
    }

    @Test
    void testComparacoes() {
        assertEquals(StatusPagamento.FINALIZADO, StatusPagamento.FINALIZADO);
        assertNotEquals(StatusPagamento.FINALIZADO, StatusPagamento.PENDENTE);
    }
}