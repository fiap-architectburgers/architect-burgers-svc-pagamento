package com.example.gomesrodris.archburgers.adapters.datasource;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

class PagamentoRepositorioImplIT {

    private static RealDatabaseTestHelper realDatabase;
    private MongoDatabaseConnection mongoDatabaseConnection;

    private PagamentoRepositoryNoSqlImpl repository;

    private String idPagamento;
    private Integer idPedido = 1;
    private IdFormaPagamento formaPagamento = new IdFormaPagamento("MERCADO_PAGO");
    private ValorMonetario valor = new ValorMonetario("100");
    private final LocalDateTime dataHora = LocalDateTime.now();
    private String codigoPagamentoCliente = "12345";
    private String idPedidoSistemaExterno = "123";


    @BeforeAll
    static void beforeAll() throws Exception {
        realDatabase = new RealDatabaseTestHelper();
        realDatabase.beforeAll();
    }

    @AfterAll
    static void afterAll() {
        realDatabase.afterAll();
    }

    @BeforeEach
    void setUp() {
        mongoDatabaseConnection = realDatabase.getConnection();
        repository = new PagamentoRepositoryNoSqlImpl(mongoDatabaseConnection);
    }

    @AfterEach
    void tearDown() throws Exception {
        mongoDatabaseConnection.close();
    }

    @Test
    void validaSalvarPagamento() {
        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);
        Pagamento pagamentoSalvo = repository.salvarPagamento(pagamento);

        this.idPagamento = pagamentoSalvo.id();

        Assertions.assertThat(pagamentoSalvo.id()).isNotNull();
        Assertions.assertThat(pagamentoSalvo.formaPagamento().codigo().toString()).isEqualTo(new IdFormaPagamento("MERCADO_PAGO").codigo().toString());
        Assertions.assertThat(pagamentoSalvo.valor().asBigDecimal().toString()).isEqualTo(new ValorMonetario("100").asBigDecimal().toString());
        Assertions.assertThat(pagamentoSalvo.dataHoraCriacao()).isEqualTo(dataHora);
        Assertions.assertThat(pagamentoSalvo.dataHoraAtualizacao()).isEqualTo(dataHora);
        Assertions.assertThat(pagamentoSalvo.codigoPagamentoCliente()).isEqualTo("12345");
        Assertions.assertThat(pagamentoSalvo.idPedidoSistemaExterno()).isEqualTo("123");

        Assertions.assertThat(repository.findPagamentoByPedido(idPedido)).isNotNull();
    }

    @Test
    void validaFindPagamentoByPedidoNull() {
        Pagamento pagamentoSalvo = repository.findPagamentoByPedido(null);
        Assertions.assertThat(pagamentoSalvo).isNull();
    }

    @Test
    void validaFindPagamentoByPedidoZerado() {
        Pagamento pagamentoSalvo = repository.findPagamentoByPedido(0);
        Assertions.assertThat(pagamentoSalvo).isNull();
    }

    @Test
    void validaFindPagamentoByPedido() {
        this.idPedido = 2;
        validaSalvarPagamento();
        Pagamento pagamentoSalvo = repository.findPagamentoByPedido(idPedido);

        Assertions.assertThat(pagamentoSalvo.id()).isNotNull();
        Assertions.assertThat(pagamentoSalvo.formaPagamento().codigo().toString()).isEqualTo(new IdFormaPagamento("MERCADO_PAGO").codigo().toString());
        Assertions.assertThat(pagamentoSalvo.valor().asBigDecimal().toString()).isEqualTo(new ValorMonetario("100").asBigDecimal().toString());
        Assertions.assertThat(pagamentoSalvo.dataHoraCriacao()).isEqualTo(dataHora);
        Assertions.assertThat(pagamentoSalvo.dataHoraAtualizacao()).isEqualTo(dataHora);
        Assertions.assertThat(pagamentoSalvo.codigoPagamentoCliente()).isEqualTo("12345");
        Assertions.assertThat(pagamentoSalvo.idPedidoSistemaExterno()).isEqualTo("123");
    }

    @Test
    void validaFindPagamentoByPedidoInvalido() {
        Assertions.assertThat(repository.findPagamentoByPedido(999)).isNull();
    }

    @Test
    void validaUpdateStatus() {
        this.idPedido = 3;
        validaSalvarPagamento();
        Pagamento pagamento = repository.findPagamentoByPedido(idPedido);
        LocalDateTime dataAtualizacao = LocalDateTime.now();
        Pagamento pagamentoFinalizado = pagamento.finalizar(dataAtualizacao);
        repository.updateStatus(pagamentoFinalizado);

        Pagamento pagamentoAtualizado = repository.findPagamentoByPedido(idPedido);


        Assertions.assertThat(pagamentoAtualizado.id()).isNotNull();
        Assertions.assertThat(pagamentoAtualizado.formaPagamento().codigo().toString()).isEqualTo(new IdFormaPagamento("MERCADO_PAGO").codigo().toString());
        Assertions.assertThat(pagamentoAtualizado.valor().asBigDecimal().toString()).isEqualTo(new ValorMonetario("100").asBigDecimal().toString());
        Assertions.assertThat(pagamentoAtualizado.dataHoraCriacao()).isEqualTo(dataHora);
        Assertions.assertThat(pagamentoAtualizado.dataHoraAtualizacao()).isEqualTo(dataAtualizacao);
        Assertions.assertThat(pagamentoAtualizado.codigoPagamentoCliente()).isEqualTo("12345");
        Assertions.assertThat(pagamentoAtualizado.idPedidoSistemaExterno()).isEqualTo("123");
        Assertions.assertThat(pagamentoAtualizado.status().name()).isEqualTo(StatusPagamento.FINALIZADO.name());
    }

    @Test
    void deletePagamentoByIdPedido() {
        this.idPedido = 4;
        validaSalvarPagamento();

        Assertions.assertThat(repository.findPagamentoByPedido(idPedido)).isNotNull();
        repository.deletePagamentoByIdPedido(idPedido);
        Pagamento pagamentoAtualizado = repository.findPagamentoByPedido(idPedido);
        Assertions.assertThat(pagamentoAtualizado).isNull();
    }

}