package rs.ac.bg.fon.is.hotel_reservation.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RezervacijaNotFoundException extends RuntimeException {
    public RezervacijaNotFoundException(String message){
        super(message);
    }
}
