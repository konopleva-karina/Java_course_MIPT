package org.mipt.service.db;


import lombok.AllArgsConstructor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;


@AllArgsConstructor
public final class DropAllObjects {
    private final SimpleJdbcTemplate source;

    private String getSQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Main.class.getResourceAsStream(name),
                        StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public void delete(String name) throws SQLException, IOException {
        String sql = getSQL(name);
        source.statement(stmt -> {
            stmt.execute(sql);
        });
    }
}
