package com.example.gomesrodris.archburgers.testUtils;

import com.example.gomesrodris.archburgers.adapters.datasource.MongoDatabaseConnection;
import com.example.gomesrodris.archburgers.adapters.datasource.PagamentoRepositoryNoSqlImpl;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MongoDBContainer;

public class RealDatabaseTestHelper {

    private static RealDatabaseTestHelper realDatabase;
    private MongoDatabaseConnection mongoDatabaseConnection;

    private PagamentoRepositoryNoSqlImpl repository;

    private MongoDBContainer mongoDBContainer = new MongoDBContainer(
            "mongo:latest"
    );

    public void beforeAll() throws Exception {
        mongoDBContainer.start();

    }

    public void afterAll() {
        mongoDBContainer.stop();
    }

    public MongoDatabaseConnection getConnection() {
        return new MongoDatabaseConnection(getConnectionString(), "pagamentos");
    }

    @BeforeEach
    void setUp() {
        mongoDatabaseConnection = realDatabase.getConnection();
        repository = new PagamentoRepositoryNoSqlImpl(mongoDatabaseConnection);
    }

    public void start() {
        mongoDBContainer.start();
    }

    public void stop() {
        mongoDBContainer.stop();
    }

    public String getConnectionString() {
        return mongoDBContainer.getReplicaSetUrl("pagamentos");
    }
}
