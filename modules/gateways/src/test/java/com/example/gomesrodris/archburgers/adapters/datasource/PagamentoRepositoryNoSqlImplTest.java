package com.example.gomesrodris.archburgers.adapters.datasource;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.assertj.core.api.Assertions;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoRepositoryNoSqlImplTest {

    @InjectMocks
    private PagamentoRepositoryNoSqlImpl repository;

    private final MongoDatabaseConnection databaseConnection = mock(MongoDatabaseConnection.class);

    @Mock
    FindIterable<Document> findIterable;

    @Mock
    private MongoCollection<Document> mongoCollection;

    @Mock
    private MongoClient mongoClient;

    @Mock
    MongoDatabase database;

    @Mock
    InsertOneResult result;

    @Mock
    BsonValue insertedId;

    @Mock
    BsonObjectId bsonObjectId;

    @BeforeEach
    void setUp() {
        repository = new PagamentoRepositoryNoSqlImpl(databaseConnection);
    }

    @Test
    void getFindByPedidoNaoEncontrado() throws SQLException {
        when(this.databaseConnection.getConnection()).thenReturn(mongoClient);
        when(this.database.getCollection(any())).thenReturn(mongoCollection);
        when(mongoCollection.find(eq("idPedido", 35))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);
        when(mongoClient.getDatabase(any())).thenReturn(database);

        Assertions.assertThat(repository.findPagamentoByPedido(35)).isNull();
    }

    @Test
    public void testFindPagamentoByPedido() {
        Document docPagamento = new Document();

        docPagamento.append("_id", new ObjectId())
                .append("formaPagamento", IdFormaPagamento.DINHEIRO.codigo().toString())
                .append("idPedido", 35)
                .append("status", StatusPagamento.PENDENTE.name().toString())
                .append("valor", new ValorMonetario("100").asBigDecimal().toString())
                .append("dataHoraCriacao", LocalDateTime.now().toString())
                .append("dataHoraAtualizacao", LocalDateTime.now().toString())
                .append("codigoPagamentoCliente", "12345")
                .append("idPedidoSistemaExterno", "123");

        when(this.databaseConnection.getConnection()).thenReturn(mongoClient);
        when(this.database.getCollection(any())).thenReturn(mongoCollection);
        when(mongoCollection.find(eq("idPedido", 35))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(docPagamento);
        when(mongoClient.getDatabase(any())).thenReturn(database);

        Pagamento pagamentoEncontrado = repository.findPagamentoByPedido(35);

        assertThat(pagamentoEncontrado).isNotNull();
        assertThat(pagamentoEncontrado.formaPagamento().codigo().toString()).isEqualTo(IdFormaPagamento.DINHEIRO.codigo().toString());
        assertThat(pagamentoEncontrado.idPedido()).isEqualTo(35);

    }

    @Test
    public void testSalvarPagamento() {
        Document docPagamento = new Document();

        docPagamento.append("_id", new ObjectId())
                .append("formaPagamento", IdFormaPagamento.DINHEIRO.codigo().toString())
                .append("idPedido", 35)
                .append("status", StatusPagamento.PENDENTE.name().toString())
                .append("valor", new ValorMonetario("100").asBigDecimal().toString())
                .append("dataHoraCriacao", LocalDateTime.now().toString())
                .append("dataHoraAtualizacao", LocalDateTime.now().toString())
                .append("codigoPagamentoCliente", "12345")
                .append("idPedidoSistemaExterno", "123");

        when(this.databaseConnection.getConnection()).thenReturn(mongoClient);
        when(this.database.getCollection(any())).thenReturn(mongoCollection);
        when(mongoClient.getDatabase(any())).thenReturn(database);
        when(mongoCollection.insertOne(any())).thenReturn(result);
        when(result.getInsertedId()).thenReturn(insertedId);
        when(insertedId.asObjectId()).thenReturn(bsonObjectId);
        when(insertedId.asObjectId().toString()).thenReturn("id001");

        String idPagamento;
        Integer idPedido = 35;
        IdFormaPagamento formaPagamento = new IdFormaPagamento("DINHEIRO");
        ValorMonetario valor = new ValorMonetario("100");
        final LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "12345";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);
        Pagamento pagamentoSalvo = repository.salvarPagamento(pagamento);



        assertThat(pagamentoSalvo).isNotNull();
        assertThat(pagamentoSalvo.formaPagamento().codigo().toString()).isEqualTo(IdFormaPagamento.DINHEIRO.codigo().toString());
        assertThat(pagamentoSalvo.idPedido()).isEqualTo(35);
        assertThat(pagamentoSalvo.id()).isNotNull();
        assertThat(pagamentoSalvo.id()).isEqualTo("id001");
    }

    @Test
    void getFindByPedidoError() throws SQLException {
        when(this.databaseConnection.getConnection()).thenReturn(null);
        assertThrows(RuntimeException.class, () -> repository.findPagamentoByPedido(0));
    }

    @Test
    void salvarPagamento() throws SQLException {
        when(this.databaseConnection.getConnection()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> repository.salvarPagamento(null));
    }

    @Test
    void deletePagamento() throws SQLException {
        when(this.databaseConnection.getConnection()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> repository.deletePagamentoByIdPedido(null));
    }

    @Test
    void updatePagamento() throws SQLException {
        when(this.databaseConnection.getConnection()).thenReturn(null);
        assertThrows(RuntimeException.class, () -> repository.updateStatus(null));
    }

}
