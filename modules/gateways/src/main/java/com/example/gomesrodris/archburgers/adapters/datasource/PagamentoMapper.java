package com.example.gomesrodris.archburgers.adapters.datasource;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PagamentoMapper {

    public static Pagamento toPagamento(Document document) {
        if (document == null) {
            return null;
        }

        String dataHoraCriacao = document.getString("dataHoraCriacao");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Padrão ISO 8601
        LocalDateTime timeCriacao = LocalDateTime.parse(dataHoraCriacao, formatter);

        String dataHoraAtualizacao = document.getString("dataHoraAtualizacao");
        LocalDateTime timeAtualizacao = LocalDateTime.parse(dataHoraAtualizacao, formatter);

        return new Pagamento(
                document.getObjectId("_id").toString(),
                document.getInteger("idPedido"),
                IdFormaPagamento.valueOf(document.getString("formaPagamento")),
                StatusPagamento.valueOf(document.getString("status")),
                new ValorMonetario(document.getString("valor")),
                timeCriacao,
                timeAtualizacao,
                document.getString("codigoPagamentoCliente"),
                document.getString("idPedidoSistemaExterno"));

    }
}
