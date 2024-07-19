package rs.ac.bg.fon.is.hotel_reservation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class RezervacijaDTO {
    private Long id;
    private String email;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;
    private String promoKod;
    private Long sobaId;
    private String token;
    private List<GostDTO> gosti;
    private double ukupnaCena;
    private boolean aktivna;

}
