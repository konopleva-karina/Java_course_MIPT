package org.mipt.domain;

import lombok.Data;

@Data
public class TicketFlight {
    private final String ticketNo;
    private final long flightId;
    private final String fareConditions;
    private final double amount;
}
