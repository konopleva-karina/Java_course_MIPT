package org.mipt.domain;


import lombok.Data;
import java.sql.Timestamp;
import java.text.ParseException;

@Data
public class Booking {
    private final String bookRef;
    private final Timestamp bookDate;
    private final double totalAmount;

    public Booking(String bookRef, Timestamp bookDate, double totalAmount) {
        this.bookRef = bookRef;
        this.bookDate = bookDate;
        this.totalAmount = totalAmount;
    }

    public Booking(String bookRef, String bookDate, double totalAmount) throws ParseException {
        this.bookRef = bookRef;
        this.bookDate = Timestamp.valueOf(bookDate);
        this.totalAmount = totalAmount;
    }
}
