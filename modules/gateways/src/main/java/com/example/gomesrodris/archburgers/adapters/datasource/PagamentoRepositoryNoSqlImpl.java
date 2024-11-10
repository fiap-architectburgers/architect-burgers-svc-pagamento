package com.example.gomesrodris.archburgers.adapters.datasource;

import com.example.gomesrodris.archburgers.domain.datasource.PagamentoDataSource;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

import static com.mongodb.client.model.Filters.eq;

/**
 * Implementation of the Repository based on a non-relational database via JDBC
 */

@Qualifier("noSql")
public class PagamentoRepositoryNoSqlImpl implements PagamentoDataSource {

    private final MongoDatabaseConnection databaseConnection;

    @Autowired
    public PagamentoRepositoryNoSqlImpl(MongoDatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Pagamento findPagamentoByPedido(Integer idPedido) {

        MongoClientSettings settings = this.databaseConnection.getMongoClientSettings();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {

                MongoDatabase database = mongoClient.getDatabase(this.databaseConnection.getDatabase());
                MongoCollection<Document> collection = database.getCollection("pagamentos");

                Document result = collection.find(eq("idPedido", idPedido))
                        .first();

                if (result == null) {
                    return null;
                } else {
                    return PagamentoMapper.toPagamento(result);
                }

            } catch (MongoException e) {
                throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Pagamento salvarPagamento(Pagamento pagamento) {

        MongoClientSettings settings = this.databaseConnection.getMongoClientSettings();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {

                MongoDatabase database = mongoClient.getDatabase(this.databaseConnection.getDatabase());
                MongoCollection<Document> collection = database.getCollection("pagamentos");

                Document docPagamento = new Document();

                docPagamento.append("_id", new ObjectId())
                        .append("formaPagamento", pagamento.formaPagamento().toString())
                        .append("idPedido", pagamento.idPedido())
//                      .append("id", pagamento.id())
//                      .append("formaPagamento", pagamento.formaPagamento().codigo())
                        .append("status", pagamento.status().toString())
                        .append("valor", pagamento.valor().asBigDecimal().toString())
                        .append("dataHoraCriacao", pagamento.dataHoraCriacao().toString())
                        .append("dataHoraAtualizacao", pagamento.dataHoraAtualizacao().toString())
                        .append("codigoPagamentoCliente", pagamento.codigoPagamentoCliente())
                        .append("idPedidoSistemaExterno", pagamento.idPedidoSistemaExterno());

//                docPagamento.append("pagamento", docPagamento);

                InsertOneResult result = collection.insertOne(docPagamento);

                pagamento.withId(result.getInsertedId().asObjectId().toString());

                mongoClient.close();

                return pagamento;
            } catch (MongoException e) {
                throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void updateStatus(Pagamento pagamento) {

        MongoClientSettings settings = this.databaseConnection.getMongoClientSettings();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {

                MongoDatabase database = mongoClient.getDatabase(this.databaseConnection.getDatabase());
                MongoCollection<Document> collection = database.getCollection("pagamentos");

                Document query = collection.find(eq("_id", new ObjectId(pagamento.id())))
                        .first();

//                Document query = new Document().append("_id", pagamento.id());
                Bson updates = Updates.combine(
                        Updates.set("status", pagamento.status().toString()),
                        Updates.set("dataHoraAtualizacao", LocalDateTime.now()));

                UpdateOptions options = new UpdateOptions().upsert(true);

                UpdateResult result = collection.updateOne(query, updates, options);
                System.out.println("Modified document count: " + result.getModifiedCount());
                System.out.println("Upserted id: " + result.getUpsertedId());

            } catch (MongoException e) {
                throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void deletePagamentoByIdPedido(Integer idPedido) {
        MongoClientSettings settings = this.databaseConnection.getMongoClientSettings();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {

                MongoDatabase database = mongoClient.getDatabase(this.databaseConnection.getDatabase());
                MongoCollection<Document> collection = database.getCollection("pagamentos");

                Document query = new Document().append("idPedido", idPedido);
                    DeleteResult result = collection.deleteOne(query);
                    System.out.println("Deleted document count: " + result.getDeletedCount());

            } catch (MongoException e) {
                throw new RuntimeException("(" + this.getClass().getSimpleName() + ") Database error: " + e.getMessage(), e);
            }
        }
    }
}