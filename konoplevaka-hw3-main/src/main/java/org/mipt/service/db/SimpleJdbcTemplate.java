package org.mipt.service.db;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Objects;

public final class SimpleJdbcTemplate {
    private final OracleConnectionPoolDataSource connectionPool;

    public SimpleJdbcTemplate(OracleConnectionPoolDataSource connectionPool) throws SQLException {
        this.connectionPool = connectionPool;
        this.connectionPool.setURL("jdbc:oracle:thin:@localhost:1521/xe");
        this.connectionPool.setUser("system");
        this.connectionPool.setPassword("oracle");
    }

    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T object) throws SQLException, ParseException;
    }

    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T object) throws SQLException, IOException;
    }

    public void connection(SQLConsumer<? super PooledConnection> consumer) throws SQLException, IOException {
        Objects.requireNonNull(consumer);
        PooledConnection conn = connectionPool.getPooledConnection("system", "oracle");
        consumer.accept(conn);
    }

    public <R> R connection(SQLFunction<? super PooledConnection, ? extends R> function)
            throws SQLException, ParseException {
        Objects.requireNonNull(function);
        PooledConnection conn = connectionPool.getPooledConnection("system", "oracle");
        return function.apply(conn);
    }

    public <R> R statement(SQLFunction<? super Statement, ? extends R> function) throws SQLException, ParseException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (Statement stmt = conn.getConnection().createStatement()) {
                return function.apply(stmt);
            }
        });
    }

    public void statement(SQLConsumer<? super Statement> consumer) throws SQLException, IOException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (Statement stmt = conn.getConnection().createStatement()) {
                consumer.accept(stmt);
            }
        });
    }

    public <R> R preparedStatement(String sql, SQLFunction<? super PreparedStatement, ? extends R> function)
            throws SQLException, ParseException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (PreparedStatement stmt = conn.getConnection().prepareStatement(sql)) {
                return function.apply(stmt);
            }
        });
    }

    public void preparedStatement(String sql, SQLConsumer<? super PreparedStatement> consumer)
            throws SQLException, IOException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (PreparedStatement stmt = conn.getConnection().prepareStatement(sql)) {
                consumer.accept(stmt);
            }
        });
    }
}
