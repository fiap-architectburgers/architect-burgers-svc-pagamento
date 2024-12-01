package com.example.gomesrodris.archburgers.adapters.datasource;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.testUtils.RealDatabaseTestHelper;
import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.core.env.Environment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MongoDatabaseConnectionIT {

    private static RealDatabaseTestHelper realDatabase;
    private MongoDatabaseConnection mongoDatabaseConnection;


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
    void setUp() throws Exception {
        mongoDatabaseConnection = realDatabase.getConnection();
    }

    @AfterEach
    void tearDown() {
        mongoDatabaseConnection.getConnection().close();
    }


    @Test
    public void getConnectionThrowsSQLException() throws Exception {
        Environment environment = new StaticEnvironment(Map.of(
                "archburgers.datasource-mongodb.database", "pagamentos"
        ));

        Assertions.assertThatThrownBy(() -> {
            new MongoDatabaseConnection(environment);
        }).hasMessageContaining("datasource-mongodb.uri env is missing");


    }

    @Test
    public void getConnectionSuccess() throws Exception {

        Environment env = new StaticEnvironment(Map.of(
                "archburgers.datasource-mongodb.uri", realDatabase.getConnectionString(),
                "archburgers.datasource-mongodb.database", "pagamentos"
        ));

        try (MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection(env)) {
            assertThat(mongoDatabaseConnection.getConnection()).isNotNull();

            var result = mongoDatabaseConnection.runInTransaction(() -> "Inside Result");
            assertThat(result).isEqualTo("Inside Result");
        }

    }

}