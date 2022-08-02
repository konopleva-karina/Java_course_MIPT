package org.mipt.service.db;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import java.io.IOException;
import java.sql.SQLException;


public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws SQLException, IOException {
        OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
        SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);

        var dbInit = new DbInit(source);
        dbInit.create();

        FillDb fillDb = new FillDb(source);
        fillDb.fill();

        var deleter = new DropAllObjects(source);
        source.statement(stmt -> {
            deleter.delete("drop_all_objects.sql");
        });
    }
}
