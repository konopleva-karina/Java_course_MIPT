package org.mipt.service.dao;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.mipt.domain.Flight;
import org.mipt.service.db.SimpleJdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public final class FlightDao {
    private final SimpleJdbcTemplate source;

    private Flight createFlight(ResultSet resultSet) throws SQLException {
        return new Flight(resultSet.getLong("flightId"),
                resultSet.getString("flightNo"),
                resultSet.getTimestamp("scheduledDeparture"),
                resultSet.getTimestamp("scheduledArrival"),
                resultSet.getString("departureAirport"),
                resultSet.getString("arrivalAirport"),
                resultSet.getString("status"),
                resultSet.getString("aircraftCode"),
                resultSet.getTimestamp("actualDeparture"),
                resultSet.getTimestamp("actualArrival")
        );
    }

    public void saveFlights(Collection<Flight> flights) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO FLIGHTS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                insertFlight -> {
                    for (Flight flight : flights) {
                        int parameterIndex = 1;
                        insertFlight.setLong(parameterIndex++, flight.getFlightId());
                        insertFlight.setString(parameterIndex++, flight.getFlightNo());
                        insertFlight.setTimestamp(parameterIndex++, flight.getScheduledDeparture());
                        insertFlight.setTimestamp(parameterIndex++, flight.getScheduledArrival());
                        insertFlight.setString(parameterIndex++, flight.getDepartureAirport());
                        insertFlight.setString(parameterIndex++, flight.getArrivalAirport());
                        insertFlight.setString(parameterIndex++, flight.getStatus());
                        insertFlight.setString(parameterIndex++, flight.getAircraftCode());
                        insertFlight.setTimestamp(parameterIndex++, flight.getActualDeparture());
                        insertFlight.setTimestamp(parameterIndex, flight.getActualArrival());
                        insertFlight.execute();
                    }
                });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/flights.csv");
        ArrayList<Flight> flights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int parameterIndex = 0;
                long flightId = Long.parseLong(data[parameterIndex++]);
                String flightNo = data[parameterIndex++];
                Timestamp scheduledDeparture = Timestamp.valueOf(data[parameterIndex++].split("\\+")[0]);
                Timestamp scheduledArrival = Timestamp.valueOf(data[parameterIndex++].split("\\+")[0]);
                String departureAirport = data[parameterIndex++];
                String arrivalAirport = data[parameterIndex++];
                String status = data[parameterIndex++];
                String aircraftCode = data[parameterIndex++];
                Timestamp actualDeparture = null;
                Timestamp actualArrival = null;

                if (data.length >= parameterIndex + 1) {
                    actualDeparture = Timestamp.valueOf(data[parameterIndex++].split("\\+")[0]);
                }

                if (data.length == parameterIndex + 1) {
                    actualArrival = Timestamp.valueOf(data[parameterIndex].split("\\+")[0]);
                }

                flights.add(new Flight(flightId, flightNo, scheduledDeparture, scheduledArrival, departureAirport,
                        arrivalAirport, status, aircraftCode, actualDeparture, actualArrival));
            }
            saveFlights(flights);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<Flight> getFlights() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<Flight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM FLIGHTS");
            while (resultSet.next()) {
                result.add(createFlight(resultSet));
            }
            return result;
        });
    }

    public ArrayList<String[]> B2() throws SQLException, ParseException {
        return source.statement(stmt -> {
            String query = "SELECT *\n" +
                    "FROM\n" +
                    "(SELECT airports.city, COUNT(flights.flightid) AS \"number of cancelled flights\"\n" +
                    "FROM flights\n" +
                    "LEFT JOIN airports\n" +
                    "ON flights.departureairport = airports.airportcode\n" +
                    "WHERE flights.status = 'Cancelled'\n" +
                    "GROUP BY airports.city\n" +
                    "ORDER BY COUNT(flights.flightid) DESC)\n" +
                    "WHERE ROWNUM <= 5";

            ResultSet resultSet = stmt.executeQuery(query);
            ArrayList<String[]> rows = new ArrayList<>();
            while (resultSet.next()) {
                String cancelled = resultSet.getString("number of cancelled flights");
                String city = resultSet.getString("CITY");
//                JSONObject json = new JSONObject(city.substring(1, city.length() - 1));
                rows.add(new String[]{city, cancelled});
            }
            return rows;
        });
    }
}
