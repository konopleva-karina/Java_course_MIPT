package org.mipt.service.dao;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mipt.domain.Aircraft;
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
import static org.mipt.service.dao.TestData.BOEING;
import static org.mipt.service.dao.TestData.CESSNA;

public class AircraftDaoTest {
    private OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
    private SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
    private AircraftDao dao = new AircraftDao(source);

    public AircraftDaoTest() throws SQLException {
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

    private int getAircraftCount() throws SQLException, ParseException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM AIRCRAFTS");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Aircraft> getTestAircrafts() {
        return Arrays.asList(BOEING, CESSNA);
    }

    @Test
    void fillDbTest() throws IOException {
        dao.saveFromCSV();
    }

    @Test
    void saveAircrafts() throws SQLException, IOException, ParseException {
        Collection<Aircraft> testAircrafts = getTestAircrafts();
        assertEquals(0, getAircraftCount());
        dao.saveAircrafts(testAircrafts);
        assertEquals(testAircrafts.size(), getAircraftCount());
    }

    @Test
    void getAircrafts() throws SQLException, IOException, ParseException {
        Collection<Aircraft> testAircrafts = getTestAircrafts();
        dao.saveAircrafts(testAircrafts);
        Set<Aircraft> aircrafts = dao.getAircrafts();
        assertNotSame(testAircrafts, aircrafts);
        System.out.println(aircrafts);
        assertEquals(new HashSet<>(testAircrafts), aircrafts);
    }
}
