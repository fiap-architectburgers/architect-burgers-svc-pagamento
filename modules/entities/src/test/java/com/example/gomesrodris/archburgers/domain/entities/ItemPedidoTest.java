package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemPedidoTest {

    @Test
    void constructorTest() {
        ItemCardapio itemCardapio = new ItemCardapio(1, TipoItemCardapio.LANCHE, "Lanche 1", "Lanchão Ogro", new ValorMonetario("30.0"));
        ItemPedido itemPedido = new ItemPedido(1, itemCardapio);

        assertEquals(1, itemPedido.numSequencia());
        assertEquals(itemCardapio, itemPedido.itemCardapio());
    }

    @Test
    void equalsAndHashCodeTest() {
        ItemCardapio itemCardapio = new ItemCardapio(1, TipoItemCardapio.LANCHE, "Lanche 1", "Lanchão Ogro", new ValorMonetario("30.0"));
        ItemPedido itemPedido1 = new ItemPedido(1, itemCardapio);
        ItemPedido itemPedido2 = new ItemPedido(1, itemCardapio);

        assertEquals(itemPedido1, itemPedido2);
        assertEquals(itemPedido1.hashCode(), itemPedido2.hashCode());

        // Testando com valores diferentes
        ItemPedido itemPedido3 = new ItemPedido(2, itemCardapio);
        assertNotEquals(itemPedido1, itemPedido3);
    }
}

