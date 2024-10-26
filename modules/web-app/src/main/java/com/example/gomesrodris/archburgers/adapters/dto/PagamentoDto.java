package com.example.gomesrodris.archburgers.adapters.dto;

public record PagamentoDto(
        Integer id,
        Integer idPedido,
        String formaPagamento,
        String status,
        ValorMonetarioDto valor,
        long dataHoraCriacao,
        long dataHoraAtualizacao,
        String codigoPagamentoCliente,
        String idPedidoSistemaExterno
) {

}
