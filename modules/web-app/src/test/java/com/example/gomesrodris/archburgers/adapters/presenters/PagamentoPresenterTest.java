package com.example.gomesrodris.archburgers.adapters.presenters;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.adapters.dto.PagamentoDto;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PagamentoPresenterTest {

    @Test
    public void testEntityToPresentationDto_ValidPagamento() {
        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        long dataHoraEmMilissegundos = dataHora.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        String codigoPagamentoCliente = "123456";
        String idPedidoSistemaExterno = "123";
        Pagamento pagamento = new Pagamento("233", idPedido, formaPagamento, StatusPagamento.FINALIZADO, valor, dataHora, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);

        // Call the method
        PagamentoDto dto = PagamentoPresenter.entityToPresentationDto(pagamento);

        // Verify the DTO values
        assertEquals(233, dto.id());
        assertEquals(idPedido, dto.idPedido());
        assertEquals(formaPagamento.toString(), dto.formaPagamento());
        assertEquals(dataHoraEmMilissegundos, dto.dataHoraCriacao());
        assertEquals(dataHoraEmMilissegundos, dto.dataHoraAtualizacao());
        assertEquals(codigoPagamentoCliente, dto.codigoPagamentoCliente());
        assertEquals(idPedidoSistemaExterno, dto.idPedidoSistemaExterno());
    }

    @Test
    public void testEntityToPresentationDto_NullPagamento() {
        // Test with a null Pagamento object
        Pagamento pagamento = null;
        assertThrows(NullPointerException.class, () -> PagamentoPresenter.entityToPresentationDto(pagamento));
    }
}