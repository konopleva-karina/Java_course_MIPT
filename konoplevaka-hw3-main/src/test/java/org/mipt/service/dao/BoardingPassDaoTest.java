package org.mipt.service.dao;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mipt.domain.BoardingPass;
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
import static org.mipt.service.dao.TestData.BOARDINGPASS1;
import static org.mipt.service.dao.TestData.BOARDINGPASS2;

public class BoardingPassDaoTest {
    private OracleConnectionPoolDataSource pool = new OracleConnectionPoolDataSource();
    private SimpleJdbcTemplate source = new SimpleJdbcTemplate(pool);
    private BoardingPassDao dao = new BoardingPassDao(source);

    public BoardingPassDaoTest() throws SQLException {
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

    private int getBoardingPassCount() throws SQLException, ParseException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM BOARDINGPASSES");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<BoardingPass> getTestBoardingPasses() {
        return Arrays.asList(BOARDINGPASS1, BOARDINGPASS2);
    }

    @Test
    void fillDbTest() throws IOException {
        dao.saveFromCSV();
    }

    @Test
    void saveBoardingPasses() throws SQLException, IOException, ParseException {
        Collection<BoardingPass> testBoardingPasses = getTestBoardingPasses();
        assertEquals(0, getBoardingPassCount());
        dao.saveBoardingPasses(testBoardingPasses);
        System.out.println(getBoardingPassCount());
        assertEquals(testBoardingPasses.size(), getBoardingPassCount());
    }

    @Test
    void getBoardingPasses() throws SQLException, IOException, ParseException {
        Collection<BoardingPass> testBoardingPasses = getTestBoardingPasses();
        dao.saveBoardingPasses(testBoardingPasses);
        Set<BoardingPass> boardingPasses = dao.getBoardingPasses();
        assertNotSame(testBoardingPasses, boardingPasses);
        assertEquals(new HashSet<>(testBoardingPasses), boardingPasses);
    }
}
