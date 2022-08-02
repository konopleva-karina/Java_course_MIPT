package org.mipt.domain;

import lombok.Data;

@Data
public class BoardingPass {
    private final String ticketNo;
    private final long flightId;
    private final int boardingNo;
    private final String seatNo;
}
