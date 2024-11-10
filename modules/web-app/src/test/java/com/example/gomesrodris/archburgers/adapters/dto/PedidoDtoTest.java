package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.adapters.testUtils.TestLocale;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PedidoDtoTest {

    private PedidoDto pedidoDto;
    IdFormaPagamento formaPagamento = IdFormaPagamento.DINHEIRO;
    LocalDateTime dataHora = LocalDateTime.now();
    long dataHoraEmMilissegundos = dataHora.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    @BeforeEach
    void setUp() {
        ItemCardapio itemCardapio = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                new ValorMonetario("23.50"));

        ItemCardapio itemCardapio2 = new ItemCardapio(33, TipoItemCardapio.SOBREMESA, "Torta de chocolate",
                "Torta sem graça",
                new ValorMonetario("53.85"));

        List<ItemPedidoDto> itens = new ArrayList<>();

        ItemPedidoDto ip1 = new ItemPedidoDto(1, 21, "tipo", "nome", "descricao", new ValorMonetarioDto("10.00", "R$ 10,00"));
        ItemPedidoDto ip2 = new ItemPedidoDto(2, 33, "tipo2", "nome2", "descricao2", new ValorMonetarioDto("20.00", "R$ 20,00"));
        itens.add(ip1);
        itens.add(ip2);


        pedidoDto = new PedidoDto(11,
                222,
                null,
                itens,
                "Sem cebola",
                StatusPedido.RECEBIDO.toString(),
                formaPagamento.toString(),
                new ValorMonetarioDto("100.00", "R$ 100,00"),
                dataHoraEmMilissegundos);
    }

    @Test
    void validateFields() {
        assertThat(pedidoDto.id()).isEqualTo(11);
        assertThat(pedidoDto.idClienteIdentificado()).isEqualTo(222);
        assertThat(pedidoDto.nomeClienteNaoIdentificado()).isNull();
        assertThat(pedidoDto.itens()).hasSize(2);
        assertThat(pedidoDto.status()).isEqualTo(StatusPedido.RECEBIDO.toString());
        assertThat(pedidoDto.formaPagamento()).isEqualTo(formaPagamento.toString());
        assertThat(pedidoDto.dataHoraCarrinhoCriado()).isEqualTo(dataHoraEmMilissegundos);


    }
}