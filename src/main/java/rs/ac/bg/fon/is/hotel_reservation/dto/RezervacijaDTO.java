package rs.ac.bg.fon.is.hotel_reservation.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class RezervacijaDTO {
    private Long id;

    @Email(message = "Email mora biti validan")
    @NotEmpty(message = "Email je obavezan")
    private String email;

    @NotNull(message = "Datum početka je obavezan")
    @Future(message = "Datum početka mora biti u budućnosti")
    private LocalDate datumPocetka;

    @NotNull(message = "Datum završetka je obavezan")
    @Future(message = "Datum završetka mora biti u budućnosti")
    private LocalDate datumZavrsetka;

    private String promoKod;

    private double ukupnaCena;
    private boolean aktivna;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    @NotNull(message = "Gosti su obavezni")
    private List<@Valid GostDTO> gosti;

    @NotNull(message = "Soba je obavezna")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SobaDTO soba;
}
