package org.mipt.service.dao;


import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mipt.domain.Airport;
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
import static org.mipt.service.dao.TestData.YKS;
import static org.mipt.service.dao.TestData.SVO;

public class AirportDaoTest {
    private OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
    private SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
    private AirportDao dao = new AirportDao(source);

    public AirportDaoTest() throws SQLException {
    }

//    @BeforeEach
//    void setupDB() throws IOException, SQLException {
//        new DbInit(source).create();
//    }
//
//    @AfterEach
//    void tearDownDB() throws SQLException, IOException {
//        source.statement(stmt -> {
//            new DropAllObjects(source).delete("drop_all_objects.sql");
//        });
//    }

    private int getAirportsCount() throws SQLException, ParseException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM AIRPORTS");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Airport> getTestAirports() {
        return Arrays.asList(YKS, SVO);
    }

    @Test
    void fillDbTest() throws IOException {
        dao.saveFromCSV();
    }

    @Test
    void saveAirports() throws SQLException, IOException, ParseException {
        Collection<Airport> testAirports= getTestAirports();
        assertEquals(0, getAirportsCount());
        dao.saveAirports(testAirports);
        System.out.println(getAirportsCount());
        assertEquals(testAirports.size(), getAirportsCount());
    }

    @Test
    void getAirports() throws SQLException, IOException, ParseException {
        Collection<Airport> testAirports = getTestAirports();
        dao.saveAirports(testAirports);
        Set<Airport> airports = dao.getAirports();
        assertNotSame(testAirports, airports);
        assertEquals(new HashSet<>(testAirports), airports);
    }

    @Test
    void B1() throws SQLException, ParseException {
        var answer = dao.B1();
        for (var row : answer) {
            System.out.println(row[0] + ": " + row[1]);
        }
    }
}
