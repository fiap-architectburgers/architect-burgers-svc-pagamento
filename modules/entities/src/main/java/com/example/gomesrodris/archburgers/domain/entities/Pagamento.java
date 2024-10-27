package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.time.LocalDateTime;

public record Pagamento(
        @Nullable Integer id,
        @NotNull Integer idPedido,
        @NotNull IdFormaPagamento formaPagamento,
        @NotNull StatusPagamento status,
        @NotNull ValorMonetario valor,
        @NotNull LocalDateTime dataHoraCriacao,
        @NotNull LocalDateTime dataHoraAtualizacao,
        @Nullable String codigoPagamentoCliente,
        @Nullable String idPedidoSistemaExterno
) {

    public static Pagamento registroInicial(@NotNull Integer idPedido,
                                            @NotNull IdFormaPagamento formaPagamento,
                                            @NotNull ValorMonetario valor,
                                            @NotNull LocalDateTime dataHora,
                                            @Nullable String codigoPagamentoCliente,
                                            @Nullable String idPedidoSistemaExterno) {
        return new Pagamento(null, idPedido, formaPagamento, StatusPagamento.PENDENTE,
                valor, dataHora, dataHora,
                codigoPagamentoCliente, idPedidoSistemaExterno);
    }

    public Pagamento finalizar(@NotNull LocalDateTime dataHora) {
        return new Pagamento(id, idPedido, formaPagamento, StatusPagamento.FINALIZADO,
                valor, dataHoraCriacao, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);
    }

    public Pagamento finalizar(@NotNull LocalDateTime dataHora, @NotNull String newIdSistemaExterno) {
        return new Pagamento(id, idPedido, formaPagamento, StatusPagamento.FINALIZADO,
                valor, dataHoraCriacao, dataHora, codigoPagamentoCliente, newIdSistemaExterno);
    }

    public Pagamento withId(int newId) {
        return new Pagamento(newId, idPedido, formaPagamento, status, valor, dataHoraCriacao, dataHoraAtualizacao,
                codigoPagamentoCliente, idPedidoSistemaExterno);
    }

    public String getPagamentoJson(){

        JSONObject jo = new JSONObject();
        jo.put("id", this.id());
        jo.put("idPedido", this.idPedido());
        jo.put("formaPagamento", this.formaPagamento().toString());
        jo.put("status", this.status().toString());
        jo.put("valor", this.valor().toString());
        jo.put("dataHoraCriacao", this.dataHoraCriacao().toString());
        jo.put("dataHoraAtualizacao", this.dataHoraAtualizacao().toString());
        jo.put("codigoPagamentoCliente", this.codigoPagamentoCliente());
        jo.put("idPedidoSistemaExterno", this.idPedidoSistemaExterno());

        return jo.toString();
    }
}
