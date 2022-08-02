CREATE TABLE AIRCRAFTS (
    aircraftCode CHAR(3) PRIMARY KEY,
    model VARCHAR(200) NOT NULL,
    range INTEGER NOT NULL
);

CREATE TABLE AIRPORTS (
    airportCode CHAR(3) PRIMARY KEY,
    airportName VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    coordinates VARCHAR(200) NOT NULL,
    timezone VARCHAR(200) NOT NULL
);

CREATE TABLE BOARDINGPASSES (
    ticketNo CHAR(13) NOT NULL,
    flightId NUMBER(10) NOT NULL,
    boardingNo INT NOT NULL,
    seatNo VARCHAR(4) NOT NULL,
    CONSTRAINT boardingPassesPk PRIMARY KEY(ticketNo, flightId)
);

CREATE TABLE BOOKINGS (
    bookRef CHAR(6) PRIMARY KEY,
    bookDate TIMESTAMP NOT NULL,
    totalAmount NUMERIC(10, 2) NOT NULL
);

CREATE TABLE FLIGHTS (
    flightId NUMBER(10) PRIMARY KEY,
    flightNo CHAR(6) NOT NULL,
    scheduledDeparture TIMESTAMP NOT NULL,
    scheduledArrival TIMESTAMP NOT NULL,
    departureAirport CHAR(3) NOT NULL,
    arrivalAirport CHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    aircraftCode CHAR(3) NOT NULL REFERENCES AIRCRAFTS(aircraftCode),
    actualDeparture TIMESTAMP,
    actualArrival TIMESTAMP
);

CREATE TABLE SEATS (
    aircraftCode CHAR(3) NOT NULL REFERENCES AIRCRAFTS(aircraftCode),
    seatNo VARCHAR(4) NOT NULL,
    fareConditions VARCHAR(10) NOT NULL,
    CONSTRAINT seatsPk PRIMARY KEY(aircraftCode, seatNo)
);

CREATE TABLE TICKETS (
    ticketNo CHAR(13) PRIMARY KEY,
    bookRef CHAR(6) NOT NULL REFERENCES BOOKINGS(bookRef),
    passengerId VARCHAR(20) NOT NULL,
    passengerName VARCHAR(200) NOT NULL,
    contactData VARCHAR(300) NOT NULL
);

CREATE TABLE TICKETFLIGHTS (
    ticketNo CHAR(13) NOT NULL,
    flightId NUMBER(10) NOT NULL,
    fareConditions VARCHAR(10) NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    CONSTRAINT ticketFlightsPk PRIMARY KEY(ticketNo, flightId)
);

