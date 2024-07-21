package rs.ac.bg.fon.is.hotel_reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private double ukupnaCena;
    private boolean aktivna;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token; // Include only during creation

    private List<GostDTO> gosti;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SobaDTO soba; // Always include in the response
}
