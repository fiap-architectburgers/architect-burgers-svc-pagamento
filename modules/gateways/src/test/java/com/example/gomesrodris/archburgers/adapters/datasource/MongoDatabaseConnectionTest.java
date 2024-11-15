package com.example.gomesrodris.archburgers.adapters.datasource;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApiVersion;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import liquibase.database.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MongoDatabaseConnectionTest {

    @Mock
    private Environment environment;

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoClientSettings.Builder builder;

    @Mock
    private MongoClientSettings mongoClientSettings;

    private MongoDatabaseConnection connection;

    @Test
    public void testGetMongoClientSettingsSuccess() {
        String uri = "mongodb://localhost:27017";
        MongoDatabaseConnection databaseConnection = new MongoDatabaseConnection(uri, "database");

        MongoClientSettings settings = databaseConnection.getMongoClientSettings();

        Assertions.assertNotNull(settings);
    }


    @Test
    void mongoDatabaseConnection() {

        assertThatThrownBy(() -> {
            new MongoDatabaseConnection("", "");
        }).hasMessageContaining("datasource-mongodb.uri env is missing");

        assertThatThrownBy(() -> {
            new MongoDatabaseConnection("uri", "");
        }).hasMessageContaining("datasource-mongodb.database env is missing");

        assertThatThrownBy(() -> {
            new MongoDatabaseConnection(null, "");
        }).hasMessageContaining("datasource-mongodb.uri env is missing");

        assertThatThrownBy(() -> {
            new MongoDatabaseConnection("uri", null);
        }).hasMessageContaining("datasource-mongodb.database env is missing");

//        String connectionString = "mongodb://localhost:27017";
//        when(environment.getProperty("archburgers.datasource-mongodb.uri")).thenReturn(connectionString);
//        when(environment.getProperty("archburgers.datasource-mongodb.database")).thenReturn("database");
//        when(MongoClientSettings.builder()).thenReturn(builder);
//        when(MongoClientSettings.builder().build()).thenReturn(mongoClientSettings);
//        when(MongoClients.create(any(MongoClientSettings.class))).thenReturn(mongoClient);
//        MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection(environment);


//        MongoClientSettings settings = mongoDatabaseConnection.getMongoClientSettings();

//        when(mongoDatabaseConnection.getMongoClientSettings()).thenReturn(settings);


    }


    @Test
    void environmentVariablesValidation() {
        Map<String, String> env = new HashMap<>();

        assertThatThrownBy(() -> {
            new MongoDatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("datasource-mongodb.uri env is missing");

        env.put("archburgers.datasource-mongodb.uri", "url");

    }

    @Test
    void environmentVariablesValidationSucess() throws Exception {
        String connectionString = "mongodb://localhost:27017";
        when(environment.getProperty("archburgers.datasource-mongodb.uri")).thenReturn(connectionString);
        when(environment.getProperty("archburgers.datasource-mongodb.database")).thenReturn("database");

        MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection(environment);

        MongoClientSettings settings = mongoDatabaseConnection.getMongoClientSettings();
        assertNotNull(settings);
        assertEquals(ServerApiVersion.V1, settings.getServerApi().getVersion());

        assertNotNull(mongoDatabaseConnection.getDatabase());
        assertNotNull(mongoDatabaseConnection.getConnection());

        mongoDatabaseConnection.close();
    }

}