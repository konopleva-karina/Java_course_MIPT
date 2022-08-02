package org.mipt.service.dao;

import lombok.AllArgsConstructor;
import org.mipt.domain.BoardingPass;
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
public final class BoardingPassDao {
    private final SimpleJdbcTemplate source;

    private BoardingPass createBoardingPasses(ResultSet resultSet) throws SQLException {
        return new BoardingPass(resultSet.getString("ticketNo"),
                resultSet.getLong("flightId"),
                resultSet.getInt("boardingNo"),
                resultSet.getString("seatNo")
        );
    }

    public void saveBoardingPasses(Collection<BoardingPass> boardingPasses) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO BOARDINGPASSES VALUES (?, ?, ?, ?)",
                insertBoardingPass -> {
                    for (BoardingPass boardingPass : boardingPasses) {
                        insertBoardingPass.setString(1, boardingPass.getTicketNo());
                        insertBoardingPass.setLong(2, boardingPass.getFlightId());
                        insertBoardingPass.setInt(3, boardingPass.getBoardingNo());
                        insertBoardingPass.setString(4, boardingPass.getSeatNo());
                        insertBoardingPass.execute();
                    }
                });
    }

    public void saveFromCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/boarding_passes.csv");
        ArrayList<BoardingPass> boardingPasses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String ticketNo = data[0];
                long ticketId = Long.parseLong(data[1]);
                int boardingNo = Integer.parseInt(data[2]);
                String seatNo = data[3];

                boardingPasses.add(new BoardingPass(ticketNo, ticketId, boardingNo, seatNo));
            }
            saveBoardingPasses(boardingPasses);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<BoardingPass> getBoardingPasses() throws SQLException, ParseException {
        return source.statement(stmt -> {
            Set<BoardingPass> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM BOARDINGPASSES");
            while (resultSet.next()) {
                result.add(createBoardingPasses(resultSet));
            }
            return result;
        });
    }
}
