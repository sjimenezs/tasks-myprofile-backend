package myprofile.server.schema;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SchemaUpdater {
    private Connection openConnection() throws Exception {
        Properties connectionProps = new Properties();
        connectionProps.put("user", System.getenv("DB_USER"));
        connectionProps.put("password", System.getenv("DB_PASSWORD"));
        return DriverManager.getConnection(System.getenv("DB_URL"), connectionProps);
    }

    public void update() throws Exception {
        var file = new File(System.getenv("DB_SCHEMA_BASE_FILE"));
        java.sql.Connection connection = openConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        var resourceAccesor = new FileSystemResourceAccessor(new File(System.getenv("DB_SCHEMA_BASE_FOLDER")));

        Liquibase liquibase = new liquibase.Liquibase(file.getPath(), resourceAccesor, database);
        liquibase.update(new Contexts(), new LabelExpression());
    }
}
