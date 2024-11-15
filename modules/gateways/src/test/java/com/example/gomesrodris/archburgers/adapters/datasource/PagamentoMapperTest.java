package com.example.gomesrodris.archburgers.adapters.datasource;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class PagamentoMapperTest {


    @Test
    void validarConstrutor() {

        LocalDateTime localDateTime = LocalDateTime.now();
        Document docPagamento = new Document();

        docPagamento.append("_id", new ObjectId())
                .append("formaPagamento", IdFormaPagamento.DINHEIRO.codigo())
                .append("idPedido", 5)
                .append("status", StatusPagamento.FINALIZADO)
                .append("valor", new ValorMonetario("100").asBigDecimal().toString())
                .append("dataHoraCriacao", localDateTime)
                .append("dataHoraAtualizacao", localDateTime)
                .append("codigoPagamentoCliente", 10)
                .append("idPedidoSistemaExterno", 101);
    }

    @Test
    void validarConstrutorNulo() {
        assertThat(PagamentoMapper.toPagamento(null)).isNull();
    }

//    @Test
//    void validarConstrutorNaoNulo() {
//
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Document docPagamento = new Document();
//
//        docPagamento.append("_id", new ObjectId())
//                .append("formaPagamento", IdFormaPagamento.DINHEIRO.codigo())
//                .append("idPedido", 5)
//                .append("status", StatusPagamento.FINALIZADO.name())
//                .append("valor", new ValorMonetario("100").asBigDecimal().toString())
////                .append("dataHoraCriacao", localDateTime.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
////                .append("dataHoraAtualizacao", localDateTime.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
//                .append("dataHoraCriacao", localDateTime.toString())
//                .append("dataHoraAtualizacao", localDateTime.toString())
//                .append("codigoPagamentoCliente", "10")
//                .append("idPedidoSistemaExterno", "101");
//
//        Pagamento pagamento = PagamentoMapper.toPagamento(docPagamento);
//
//        assertThat(pagamento.formaPagamento().codigo()).isEqualTo(IdFormaPagamento.DINHEIRO.codigo());
//    }

}