package rs.ac.bg.fon.is.hotel_reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class GostDTO {
    private Long id;
    private String ime;
    private String prezime;
    @JsonIgnore
    private Long rezervacijaId;
}
