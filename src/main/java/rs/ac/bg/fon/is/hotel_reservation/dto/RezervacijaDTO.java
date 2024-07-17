package rs.ac.bg.fon.is.hotel_reservation.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RezervacijaDTO {
    private Long id;
    private String email;
    private Date datumPocetka;
    private Date datumZavrsetka;
    private String promoKod;
    private Long sobaId;
    private List<Long> gostiId;
    private double ukupnaCena;
    private boolean aktivna;
}
