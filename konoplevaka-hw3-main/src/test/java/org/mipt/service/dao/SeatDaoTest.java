package org.mipt.service.dao;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mipt.domain.Seat;
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
import static org.mipt.service.dao.TestData.SEAT1;
import static org.mipt.service.dao.TestData.SEAT2;

public class SeatDaoTest {
    private OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
    private SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
    private SeatDao dao = new SeatDao(source);
    AircraftDao aircraftDao = new AircraftDao(source);

    public SeatDaoTest() throws SQLException {
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
        aircraftDao.saveFromCSV();
    }

    @AfterEach
    void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            new DropAllObjects(source).delete("drop_all_objects.sql");
        });
    }

    private int getSeatsCount() throws SQLException, ParseException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM SEATS");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Seat> getTestSeats() {
        return Arrays.asList(SEAT1, SEAT2);
    }

    @Test
    void fillDbTest() throws IOException {
        dao.saveFromCSV();
    }

    @Test
    void saveFlights() throws SQLException, IOException, ParseException {
        Collection<Seat> testFlight = getTestSeats();
        assertEquals(0, getSeatsCount());
        dao.saveSeats(testFlight);
        assertEquals(testFlight.size(), getSeatsCount());
    }

    @Test
    void getFlights() throws SQLException, IOException, ParseException {
        Collection<Seat> testFlights = getTestSeats();
        dao.saveSeats(testFlights);
        Set<Seat> boardingPasses = dao.getSeats();
        assertNotSame(testFlights, boardingPasses);
        assertEquals(new HashSet<>(testFlights), boardingPasses);
    }
}
