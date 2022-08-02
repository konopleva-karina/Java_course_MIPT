package org.mipt.service.dao;


import lombok.AllArgsConstructor;
import org.mipt.domain.Booking;
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
import java.util.Set;
import java.util.HashSet;

@AllArgsConstructor
public final class BookingDao {
    private final SimpleJdbcTemplate source;

    private Booking createBooking(ResultSet resultSet) throws SQLException, ParseException {
        return new Booking(resultSet.getString("bookRef"),
                resultSet.getTimestamp("bookDate").toString(),
                resultSet.getDouble("totalAmount")
        );
    }

    public void saveBookings(Collection<Booking> bookings) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO BOOKINGS VALUES (?, ?, ?)",
                insertBooking -> {
                    for (Booking booking : bookings) {
                        insertBooking.setString(1, booking.getBookRef());
                        insertBooking.setTimestamp(2, booking.getBookDate());
                        insertBooking.setDouble(3, booking.getTotalAmount());
                        insertBooking.execute();
                    }
                });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/bookings.csv");
        ArrayList<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String bookRef = data[0];
                Timestamp bookDate = Timestamp.valueOf(data[1].split("\\+")[0]);
                double boardingNo = Double.parseDouble(data[2]);

                bookings.add(new Booking(bookRef, bookDate, boardingNo));
            }
            saveBookings(bookings);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<Booking> getBookings() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<Booking> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM BOOKINGS");
            while (resultSet.next()) {
                result.add(createBooking(resultSet));
            }
            return result;
        });
    }
}
