package org.mipt.service.dao;


import lombok.AllArgsConstructor;
import org.mipt.domain.Aircraft;
import org.mipt.service.Parser;
import org.mipt.service.db.SimpleJdbcTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public final class AircraftDao {
    private final SimpleJdbcTemplate source;

    private Aircraft createAircrafts(ResultSet resultSet) throws SQLException {
        return new Aircraft(resultSet.getString("aircraftCode"),
                resultSet.getString("model"),
                resultSet.getInt("range"));
    }

    public void saveAircrafts(Collection<Aircraft> aircrafts) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO AIRCRAFTS(aircraftCode, model, range) VALUES (?, ?, ?)",
                insertAircraft -> {
            for (Aircraft aircraft : aircrafts) {
                insertAircraft.setString(1, aircraft.getAircraftCode());
                insertAircraft.setString(2, aircraft.getModel());
                insertAircraft.setInt(3, aircraft.getRange());
                insertAircraft.execute();
            }
        });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/aircrafts.csv");
        ArrayList<Aircraft> aircrafts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",\\S");
                String code = data[0];
                String modelData = data[1].substring(2, data[1].length() - 3);
                int range = Integer.parseInt(data[2]);

                aircrafts.add(new Aircraft(code, Parser.parseJson(modelData), range));
            }
            saveAircrafts(aircrafts);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<Aircraft> getAircrafts() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<Aircraft> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM AIRCRAFTS");
            while (resultSet.next()) {
                result.add(createAircrafts(resultSet));
            }
            return result;
        });
    }
}
