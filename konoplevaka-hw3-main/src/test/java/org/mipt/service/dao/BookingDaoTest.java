package org.mipt.service.dao;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mipt.domain.Booking;
import org.mipt.service.db.DbInit;
import org.mipt.service.db.DropAllObjects;
import org.mipt.service.db.SimpleJdbcTemplate;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mipt.service.dao.TestData.*;

public class BookingDaoTest {
    private OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
    private SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
    private BookingDao dao = new BookingDao(source);

    public BookingDaoTest() throws SQLException {
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
    }

    @AfterEach
    void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            new DropAllObjects(source).delete("drop_all_objects.sql");
        });
    }

    private int getBookingsCount() throws SQLException, ParseException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM BOOKINGS");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Booking> getTestBookings() {
        return Arrays.asList(BOOKING1, BOOKING2);
    }

    @Test
    void fillDbTest() throws IOException {
        dao.saveFromCSV();
    }

    @Test
    void saveBookings() throws SQLException, IOException, ParseException {
        Collection<Booking> testBookings = getTestBookings();
        assertEquals(0, getBookingsCount());
        dao.saveBookings(testBookings);
        assertEquals(testBookings.size(), getBookingsCount());
    }

    @Test
    void getBookings() throws SQLException, IOException, ParseException {
        Collection<Booking> testBookings = getTestBookings();
        dao.saveBookings(testBookings);
        Set<Booking> boardingPasses = dao.getBookings();
        assertNotSame(testBookings, boardingPasses);
        assertEquals(new HashSet<>(testBookings), boardingPasses);
    }
}
