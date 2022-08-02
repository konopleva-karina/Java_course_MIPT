package org.mipt.service.dao;


import org.mipt.domain.Aircraft;
import org.mipt.domain.Airport;
import org.mipt.domain.BoardingPass;
import org.mipt.domain.Booking;
import org.mipt.domain.Seat;
import org.mipt.domain.Flight;
import org.mipt.domain.TicketFlight;
import org.mipt.domain.Ticket;
import java.sql.Timestamp;

public class TestData {
    public final static Aircraft BOEING = new Aircraft(
            Integer.toString(773),
            "{\"\"en\"\": \"\"Boeing 737-300\"\", \"\"ru\"\": \"\"Боинг 737-300\"\"}"
            , 11100);

    public final static Aircraft CESSNA = new Aircraft(
            "CN1",
            "{\"\"en\"\": \"\"Cessna 208 Caravan\"\", \"\"ru\"\": \"\"Сессна 208 Караван\"\"}",
            1200);

    public final static Airport YKS = new Airport(
            "YKS",
            "{\"\"en\"\": \"\"Yakutsk Airport\"\", \"\"ru\"\": \"\"Якутск\"\"}",
            "{\"\"en\"\": \"\"Yakutsk\"\", \"\"ru\"\": \"\"Якутск\"\"}",
            "(129.77099609375,62.0932998657227)",
            "Asia/Yakutsk"
    );

    public final static Airport SVO = new Airport(
            "SVO",
            "{\"\"en\"\": \"\"Sheremetyevo International Airport\"\", \"\"ru\"\": \"\"Шереметьево\"\"}",
            "{\"\"en\"\": \"\"Moscow\"\", \"\"ru\"\": \"\"Москва\"\"}",
            "(37.4146,55.972599)",
            "Europe/Moscow"
    );

    public final static BoardingPass BOARDINGPASS1 = new BoardingPass(
            "0005435212351",30625,1,"2D"
    );

    public final static BoardingPass BOARDINGPASS2 = new BoardingPass(
            "0005435212386",30625,2,"3G"
    );

    public final static Booking BOOKING1 = new Booking(
            "00000F", Timestamp.valueOf("2017-07-05 03:12:00"), 265700.00
    );

    public final static Booking BOOKING2 = new Booking(
            "000012", Timestamp.valueOf("2017-07-14 09:02:00"), 37900.00
    );

    public final static Flight FLIGHT1 = new Flight(
        1,
        "PG0405",
        Timestamp.valueOf("2017-07-16 09:35:00"),
        Timestamp.valueOf("2017-07-16 10:30:00"),
        "DME",
        "LED",
        "Arrived",
        "321",
        Timestamp.valueOf("2017-07-16 09:44:00"),
        Timestamp.valueOf("2017-07-16 10:39:00")
    );

    public final static Flight FLIGHT2 = new Flight(
            4739,
            "PG0561",
            Timestamp.valueOf("2017-09-05 12:30:00"),
            Timestamp.valueOf("2017-09-05 14:15:00"),
        "VKO",
        "AER",
        "Scheduled",
        "763",
        null,
        null
    );

    public final static Seat SEAT1 = new Seat(
            "319", "2A", "Business"
    );

    public final static Seat SEAT2 = new Seat(
            "320","13B", "Economy"
    );

    public final static Ticket TICKET1 = new Ticket(
            "0005432001066",
            "EC1E8B",
            "7066 161500",
            "ALEKSANDR FEDOROV",
            "{\"\"email\"\": \"\"fedorov.aleksandr-091971@postgrespro.ru\"\"," +
                    " \"\"phone\"\": \"\"+70539788707\"\"}"
    );

    public final static Ticket TICKET2 = new Ticket(
            "0005432020025",
            "DA541A",
            "1451 266294",
            "SOFYA BORISOVA",
            "{\"\"phone\"\": \"\"+70404947184\"\"}"
    );

    public final static TicketFlight TICKETFLIGHT1 = new TicketFlight(
            "0005432159776",
            30625,
            "Business",
            42100.00
    );

    public final static TicketFlight TICKETFLIGHT2 = new TicketFlight(
            "0005435212357",
            30625,
            "Comfort",
            23900.00
    );
}
