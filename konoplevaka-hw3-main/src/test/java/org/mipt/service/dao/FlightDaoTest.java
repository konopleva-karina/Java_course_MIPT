package org.mipt.service.dao;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mipt.domain.Flight;
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
import static org.mipt.service.dao.TestData.FLIGHT1;
import static org.mipt.service.dao.TestData.FLIGHT2;

public class FlightDaoTest {
    private OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
    private SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
    private FlightDao dao = new FlightDao(source);
    AircraftDao aircraftDao = new AircraftDao(source);

    public FlightDaoTest() throws SQLException {
    }

    private int getFlightsCount() throws SQLException, ParseException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM FLIGHTS");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Flight> getTestFlights() {
        return Arrays.asList(FLIGHT1, FLIGHT2);
    }

    @Test
    void fillDbTest() throws IOException {
        aircraftDao.saveFromCSV();
        dao.saveFromCSV();
    }

    @Test
    void saveFlights() throws SQLException, IOException, ParseException {
        Collection<Flight> testFlight = getTestFlights();
        assertEquals(0, getFlightsCount());
        dao.saveFlights(testFlight);
        assertEquals(testFlight.size(), getFlightsCount());
    }

    @Test
    void getFlights() throws SQLException, IOException, ParseException {
        Collection<Flight> testFlights = getTestFlights();
        dao.saveFlights(testFlights);
        Set<Flight> boardingPasses = dao.getFlights();
        assertNotSame(testFlights, boardingPasses);
        assertEquals(new HashSet<>(testFlights), boardingPasses);
    }

    @Test
    void B2() throws SQLException, ParseException {
        var answer = dao.B2();
        for (var row : answer) {
                System.out.println(row[0] + ": " + row[1]);
        }
    }
}
