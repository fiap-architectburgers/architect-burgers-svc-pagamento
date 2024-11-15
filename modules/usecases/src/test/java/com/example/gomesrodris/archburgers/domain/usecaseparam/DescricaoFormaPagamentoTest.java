package com.example.gomesrodris.archburgers.domain.usecaseparam;

import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DescricaoFormaPagamentoTest {

    @Test
    void validarSucesso() {

        DescricaoFormaPagamento descricaoFormaPagamento = new DescricaoFormaPagamento(
                IdFormaPagamento.DINHEIRO, "Pagamento em dinheiro");

        assertThat(descricaoFormaPagamento.id()).isEqualTo(IdFormaPagamento.DINHEIRO);
        assertThat(descricaoFormaPagamento.descricao()).isEqualTo("Pagamento em dinheiro");
    }
}
