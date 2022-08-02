package org.mipt.service.dao;


import lombok.AllArgsConstructor;
import org.mipt.domain.Airport;
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
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@AllArgsConstructor
public final class AirportDao {
    private final SimpleJdbcTemplate source;

    private Airport createAirports(ResultSet resultSet) throws SQLException {
        return new Airport(resultSet.getString("airportCode"),
                resultSet.getString("airportName"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone")
        );
    }

    public void saveAirports(Collection<Airport> airports) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO AIRPORTS VALUES (?, ?, ?, ?, ?)",
                insertAirport -> {
                    for (Airport airport : airports) {
                        insertAirport.setString(1, airport.getAirportCode());
                        insertAirport.setString(2, airport.getAirportName());
                        insertAirport.setString(3, airport.getCity());
                        insertAirport.setString(4, airport.getCoordinates());
                        insertAirport.setString(5, airport.getTimezone());
                        insertAirport.execute();
                    }
                });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/airports.csv");
        ArrayList<Airport> airports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",\\S");
                String airportCode = data[0];
                String airportName = data[1].substring(2, data[1].length() - 3);
                String city = data[2].substring(2, data[2].length() - 3);
                StringBuilder sb = new StringBuilder(data[3]);
                sb.append(",");
                sb.append(data[4]);
                String coordinates = sb.toString();
                String timezone = data[5];

                airports.add(new Airport(airportCode, Parser.parseJson(airportName), Parser.parseJson(city),
                        coordinates, timezone));
            }
            saveAirports(airports);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<Airport> getAirports() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<Airport> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM AIRPORTS");
            while (resultSet.next()) {
                result.add(createAirports(resultSet));
            }
            return result;
        });
    }

    public ArrayList<String[]> B1() throws SQLException, ParseException {
        return source.statement(stmt -> {
            String query = "select city, LISTAGG(airportcode, ', ')\n" +
                    "    WITHIN GROUP (ORDER BY airportcode) as airports\n" +
                    "from airports\n" +
                    "group by city\n" +
                    "having count(*) > 1";
            ResultSet resultSet = stmt.executeQuery(query);
            ArrayList<String[]> rows = new ArrayList<>();
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                rows.add(new String[]{city, (String)resultSet.getObject("airports")});
            }
            return rows;
        });
    }
}
