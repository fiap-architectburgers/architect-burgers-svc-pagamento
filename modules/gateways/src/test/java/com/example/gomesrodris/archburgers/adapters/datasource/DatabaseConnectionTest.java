package com.example.gomesrodris.archburgers.adapters.datasource;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.ConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DatabaseConnectionTest {

    @Test
    void environmentVariablesValidation() {
        Map<String, String> env = new HashMap<>();

        assertThatThrownBy(() -> {
            new DatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("driverClass env is missing");

        env.put("archburgers.datasource.driverClass", "driverTest");

        assertThatThrownBy(() -> {
            new DatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("dbUrl env is missing");

        env.put("archburgers.datasource.dbUrl", "jdbc:postgresql://localhost:5432/archburgers");

        assertThatThrownBy(() -> {
            new DatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("dbUser env is missing");

        env.put("archburgers.datasource.dbUser", "burger");

        assertThatThrownBy(() -> {
            new DatabaseConnection(new StaticEnvironment(env));
        }).hasMessageContaining("dbPass env is missing");
    }

    @Test
    void validarConstrutorNulo() {
        Assertions.assertThrows(RuntimeException.class, () -> new DatabaseConnection(null, null, null, null).getConnection());
        Assertions.assertThrows(SQLException.class, () -> new DatabaseConnection(null, null, null, null).jdbcConnection());
        assertThat(new DatabaseConnection(null, null, null, null).isInTransaction()).isFalse();
        new DatabaseConnection(null, null, null, null).close();
    }


}