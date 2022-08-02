package org.mipt.service.dao;


import lombok.AllArgsConstructor;
import org.mipt.domain.Ticket;
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
public final class TicketDao {
    private final SimpleJdbcTemplate source;

    private Ticket createTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(resultSet.getString("ticketNo"),
                resultSet.getString("bookRef"),
                resultSet.getString("passengerId"),
                resultSet.getString("passengerName"),
                resultSet.getString("contactData")
        );
    }

    public void saveTickets(Collection<Ticket> tickets) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO TICKETS VALUES (?, ?, ?, ?, ?)",
                insertTicket -> {
                    for (Ticket ticket : tickets) {
                        insertTicket.setString(1, ticket.getTicketNo());
                        insertTicket.setString(2, ticket.getBookRef());
                        insertTicket.setString(3, ticket.getPassengerId());
                        insertTicket.setString(4, ticket.getPassengerName());
                        insertTicket.setString(5, ticket.getContactData());
                        insertTicket.execute();
                    }
                });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/tickets.csv");
        ArrayList<Ticket> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String ticketNo = data[0];
                String bookRef = data[1];
                String passengerId = data[2];
                String passengerName = data[3];
                String contactData = data[4];

                tickets.add(new Ticket(ticketNo, bookRef, passengerId, passengerName, contactData));
            }
            saveTickets(tickets);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<Ticket> getTickets() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<Ticket> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM TICKETS");
            while (resultSet.next()) {
                result.add(createTicket(resultSet));
            }
            return result;
        });
    }
}
