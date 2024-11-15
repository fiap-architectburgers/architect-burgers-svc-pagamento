package com.example.gomesrodris.archburgers.domain.external;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormaPagamentoRegistryTest {

    @Test
    void validarConstrutorComParametroNulo() {
        assertThrows(IllegalArgumentException.class, () -> new FormaPagamentoRegistry(null));
//        assertThrows(NullPointerException.class, () -> new FormaPagamentoRegistry(null));
    }

    @Test
    void validarConstrutorFormaDePagamentoDuplicada() {

        FormaPagamentoInternaTest formaPagamentoInternaTest =
                new FormaPagamentoInternaTest(IdFormaPagamento.DINHEIRO, "Pagamento no caixa");

        List<FormaPagamento> formasPagamentoAdicionais = new ArrayList<>();
        formasPagamentoAdicionais.add(formaPagamentoInternaTest);

        assertThrows(IllegalStateException.class, () -> new FormaPagamentoRegistry(formasPagamentoAdicionais));
    }

    @Test
    void validarFormaPagamentoExterno() {

        FormaPagamentoRegistry registry = new FormaPagamentoRegistry(new ArrayList<>());
        List<FormaPagamento> listFormaPagamento = registry.listAll().stream().toList();
        FormaPagamento formaPagamento = listFormaPagamento.get(0);

        assertEquals(formaPagamento.isIntegracaoExterna(), false);
    }

    @Test
    void validarInicioRegistroPagamentoNulo() {

        FormaPagamentoRegistry registry = new FormaPagamentoRegistry(new ArrayList<>());
        List<FormaPagamento> listFormaPagamento = registry.listAll().stream().toList();
        FormaPagamento formaPagamento = listFormaPagamento.get(0);

        assertThrows(UnsupportedOperationException.class, () -> formaPagamento.iniciarRegistroPagamento(null));
    }

    @Test
    void validarListaPadrao() {
        FormaPagamentoRegistry registry = new FormaPagamentoRegistry(new ArrayList<>());
        assertEquals(2, registry.listAll().size());
    }

    @Test
    void validarGetFormaPagamento() {
        FormaPagamentoRegistry registry = new FormaPagamentoRegistry(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> registry.getFormaPagamento(null));
//        assertThrows(NullPointerException.class, () -> registry.getFormaPagamento(null));
    }

    @Test
    void validarFormaPagamento() {
        FormaPagamentoRegistry registry = new FormaPagamentoRegistry(new ArrayList<>());
        assertEquals(IdFormaPagamento.DINHEIRO, registry.getFormaPagamento(IdFormaPagamento.DINHEIRO).id());
    }

    @Test
    void validarFormaPagamentoInterna() {
        FormaPagamentoRegistry registry = new FormaPagamentoRegistry(new ArrayList<>());

        assertEquals(IdFormaPagamento.DINHEIRO, registry.getFormaPagamento(IdFormaPagamento.DINHEIRO).id());
    }

    private record FormaPagamentoInternaTest(IdFormaPagamento id, String descricao) implements FormaPagamento {
        @Override
        public boolean isIntegracaoExterna() {
            return false;
        }

        @Override
        public InfoPagamentoExterno iniciarRegistroPagamento(Pedido pedido) {
            throw new UnsupportedOperationException("Forma de Pagamento interna");
        }
    }


}
