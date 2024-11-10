package com.example.gomesrodris.archburgers.tools.migration;

import com.example.gomesrodris.archburgers.adapters.datasource.DatabaseConnection;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseMigrationTest {

    @Mock
    private DatabaseConnection databaseConnection = mock(DatabaseConnection.class);
    // Create and configure the class under test
    DatabaseMigration migration = new DatabaseMigration(databaseConnection);
    private DatabaseMigration databaseMigration;

    private DatabaseFactory databaseFactory = mock(DatabaseFactory.class);

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        databaseMigration = new DatabaseMigration(databaseConnection);
    }

    //        verify(databaseConnection).jdbcConnection();
//        verify(logger).info("Starting Database migrations");
//        verify(logger).info("Database migration complete");
//        verify(any(Liquibase.class)).update(new Contexts(), new LabelExpression());

    @Test
    public void testRunMigrations_NullConnection() throws Exception {
        Assertions.assertThrows(NullPointerException.class, () -> migration.runMigrations());
    }

//    @Test
//    public void testRunMigrations_NullConnectionContructorManyParams() throws Exception {
//        DatabaseMigration databaseMigration = new DatabaseMigration(null, null, null, null);
//        Assertions.assertThrows(SQLException.class, () -> databaseMigration.runMigrations());
//    }



//    @Test
//    public void testMain_InvalidArguments() throws Exception {
//        String[] args = {"driver", "url", "user", "password"};
//
////        DatabaseMigration mockMigration = mock(DatabaseMigration.class);
//        //new DatabaseMigration(args[0], args[1], args[2], args[3]);
//
//        //when(new DatabaseMigration(args[0], args[1], args[2], args[3])).thenReturn(mockMigration);
//
////        DatabaseMigration.main(args);
//
//        assertThrows(SQLException.class, () -> DatabaseMigration.main(args));
////        verify(mockMigration).runMigrations();
//
//    }

//    @Test
//    public void testMain_InvalidNumberOfArguments() throws Exception {
//        String[] args = {"driver", "url"};
//        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
//
//        DatabaseMigration.main(args);
//        assertEquals("Usage: DatabaseMigration <driverClass> <dbUrl> <dbUser> <dbPass>", argumentCaptor.getValue());
//
//    }


//        migration.setLogger(logger); // Inject a mock logger

    // Execute the method
//        migration.runMigrations();

    // Verification
//        verify(databaseConnection).jdbcConnection();
//        verify(logger).info("Starting Database migrations");
//        verify(logger).info("Database migration complete");
//        verify(any(Liquibase.class)).update(new Contexts(), new LabelExpression());
}

