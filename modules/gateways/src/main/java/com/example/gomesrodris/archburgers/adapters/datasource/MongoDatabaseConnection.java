package com.example.gomesrodris.archburgers.adapters.datasource;


import com.mongodb.*;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Scope("singleton")
public class MongoDatabaseConnection implements TransactionManager, AutoCloseable{

    private Environment environment;
    private MongoClient mongoClient;

    private String uri;
    private String database;

    @Autowired
    public MongoDatabaseConnection(Environment environment) {
        this.environment = environment;
        this.uri = this.environment.getProperty("archburgers.datasource-mongodb.uri");
        this.database = environment.getProperty("archburgers.datasource-mongodb.database");

        MongoClientSettings settings = getMongoClientSettings();
        mongoClient = MongoClients.create(settings);

    }

    public MongoDatabaseConnection(String uri, String database) {
        if ((uri != null) && (!uri.equals("")))
            this.uri = uri;
        else
            throw new IllegalStateException("datasource-mongodb.uri env is missing");

        if ((database != null) && (!database.equals("")))
            this.database = database;
        else
            throw new IllegalStateException("datasource-mongodb.database env is missing");

        MongoClientSettings settings = getMongoClientSettings();
        this.mongoClient = MongoClients.create(settings);
    }


    @NotNull
    public MongoClientSettings getMongoClientSettings() {
        MongoClientSettings settings;
//        String connectionString = this.environment.getProperty("archburgers.datasource-mongodb.uri");

        String connectionString = this.uri;


        if (connectionString == null) {
            throw new IllegalStateException("datasource-mongodb.uri env is missing");
        }

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        return settings;
    }

    public String getDatabase(){
//        return environment.getProperty("archburgers.datasource-mongodb.database");
        return this.database;
    }

    public MongoClient getConnection(){
            return MongoClients.create(getMongoClientSettings());
    }

    @Override
    public <T> T runInTransaction(Supplier<T> task) throws Exception {
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            T result = task.get();
            session.commitTransaction();
            return result;
        } catch (Exception e) {
            // Rollback the transaction on error
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
