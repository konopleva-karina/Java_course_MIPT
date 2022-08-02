package org.mipt.service.db;


import lombok.AllArgsConstructor;
import org.mipt.service.dao.AircraftDao;
import org.mipt.service.dao.AirportDao;
import org.mipt.service.dao.BoardingPassDao;
import org.mipt.service.dao.BookingDao;
import org.mipt.service.dao.FlightDao;
import org.mipt.service.dao.TicketDao;
import org.mipt.service.dao.TicketFlightDao;
import org.mipt.service.dao.SeatDao;


import java.io.IOException;

@AllArgsConstructor
public class FillDb {
    private final SimpleJdbcTemplate source;

    public final void fill() throws IOException {
        AircraftDao aircraftDao = new AircraftDao(source);
        AirportDao airportDao = new AirportDao(source);
        BoardingPassDao boardingPassDao = new BoardingPassDao(source);
        BookingDao bookingDao = new BookingDao(source);
        FlightDao flightDao = new FlightDao(source);
        SeatDao seatDao = new SeatDao(source);
        TicketDao ticketDao = new TicketDao(source);
        TicketFlightDao ticketFlightDao = new TicketFlightDao(source);

        aircraftDao.saveFromCSV();
        seatDao.saveFromCSV();
        bookingDao.saveFromCSV();
        airportDao.saveFromCSV();
        boardingPassDao.saveFromCSV();
        flightDao.saveFromCSV();
        ticketDao.saveFromCSV();
        ticketFlightDao.saveFromCSV();
    }
}
