package org.mipt.service.dao;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mipt.domain.TicketFlight;
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
import static org.mipt.service.dao.TestData.TICKETFLIGHT1;
import static org.mipt.service.dao.TestData.TICKETFLIGHT2;

public class TicketFlightDaoTest {
    private OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
    private SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
    private TicketFlightDao dao = new TicketFlightDao(source);

    public TicketFlightDaoTest() throws SQLException {
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

    private int getFlightsCount() throws SQLException, ParseException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM TICKETFLIGHTS");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<TicketFlight> getTestTicketFlights() {
        return Arrays.asList(TICKETFLIGHT1, TICKETFLIGHT2);
    }

    @Test
    void fillDbTest() throws IOException {
        dao.saveFromCSV();
    }

    @Test
    void saveTicketFlightsTest() throws SQLException, IOException, ParseException {
        Collection<TicketFlight> testTickets = getTestTicketFlights();
        assertEquals(0, getFlightsCount());
        dao.saveTicketFlights(testTickets);
        assertEquals(testTickets.size(), getFlightsCount());
    }

    @Test
    void getTicketFlightsTest() throws SQLException, IOException, ParseException {
        Collection<TicketFlight> testTickets = getTestTicketFlights();
        dao.saveTicketFlights(testTickets);
        Set<TicketFlight> boardingPasses = dao.getTicketFlights();
        assertNotSame(testTickets, boardingPasses);
        assertEquals(new HashSet<>(testTickets), boardingPasses);
    }
}
