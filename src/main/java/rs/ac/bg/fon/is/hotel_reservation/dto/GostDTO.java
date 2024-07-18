package rs.ac.bg.fon.is.hotel_reservation.dto;

import lombok.Data;

@Data
public class GostDTO {
    private Long id;
    private String ime;
    private String prezime;
    private Long rezervacijaId;
}
