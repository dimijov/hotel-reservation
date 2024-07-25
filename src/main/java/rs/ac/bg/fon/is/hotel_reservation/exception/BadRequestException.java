package rs.ac.bg.fon.is.hotel_reservation.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
