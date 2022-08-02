package org.mipt.service.dao;


import lombok.AllArgsConstructor;
import org.mipt.domain.Seat;
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
public final class SeatDao {
    private final SimpleJdbcTemplate source;

    private Seat createSeat(ResultSet resultSet) throws SQLException {
        return new Seat(resultSet.getString("aircraftCode"),
                resultSet.getString("seatNo"),
                resultSet.getString("fareConditions")
        );
    }

    public void saveSeats(Collection<Seat> seats) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO SEATS VALUES (?, ?, ?)",
                insertSeat -> {
                    for (Seat seat : seats) {
                        insertSeat.setString(1, seat.getAircraftCode());
                        insertSeat.setString(2, seat.getSeatNo());
                        insertSeat.setString(3, seat.getFareConditions());
                        insertSeat.execute();
                    }
                });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/seats.csv");
        ArrayList<Seat> seats = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String aircraftCode = data[0];
                String seatNo = data[1];
                String fareConditions = data[2];

                seats.add(new Seat(aircraftCode, seatNo, fareConditions));
            }
            saveSeats(seats);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<Seat> getSeats() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<Seat> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM SEATS");
            while (resultSet.next()) {
                result.add(createSeat(resultSet));
            }
            return result;
        });
    }
}
