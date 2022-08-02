package org.mipt.service.dao;

import lombok.AllArgsConstructor;
import org.mipt.domain.TicketFlight;
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
public final class TicketFlightDao {
    private final SimpleJdbcTemplate source;

    private TicketFlight createTicketFlight(ResultSet resultSet) throws SQLException {
        return new TicketFlight(resultSet.getString("ticketNo"),
                resultSet.getLong("flightId"),
                resultSet.getString("fareConditions"),
                resultSet.getDouble("amount")
        );
    }

    public void saveTicketFlights(Collection<TicketFlight> ticketFlights) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO TICKETFLIGHTS VALUES (?, ?, ?, ?)",
                insertTicketFlight -> {
                    for (TicketFlight ticketFlight : ticketFlights) {
                        insertTicketFlight.setString(1, ticketFlight.getTicketNo());
                        insertTicketFlight.setLong(2, ticketFlight.getFlightId());
                        insertTicketFlight.setString(3, ticketFlight.getFareConditions());
                        insertTicketFlight.setDouble(4, ticketFlight.getAmount());
                        insertTicketFlight.execute();
                    }
                });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/ticket_flights.csv");
        ArrayList<TicketFlight> ticketsFlights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String ticketNo = data[0];
                long flightId = Long.parseLong(data[1]);
                String fareConditions = data[2];
                double amount = Double.parseDouble(data[3]);

                ticketsFlights.add(new TicketFlight(ticketNo, flightId, fareConditions, amount));
            }
            saveTicketFlights(ticketsFlights);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<TicketFlight> getTicketFlights() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<TicketFlight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM TICKETFLIGHTS");
            while (resultSet.next()) {
                result.add(createTicketFlight(resultSet));
            }
            return result;
        });
    }
}
