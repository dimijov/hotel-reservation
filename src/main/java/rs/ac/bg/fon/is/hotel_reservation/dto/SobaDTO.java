package rs.ac.bg.fon.is.hotel_reservation.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SobaDTO {
    private Long id;

    @NotBlank(message = "Naziv je obavezan")
    private String naziv;

    @Min(value = 1, message = "Kapacitet mora biti najmanje 1")
    private int kapacitet;

    @NotBlank(message = "Opis je obavezan")
    private String opis;

    @Min(value = 0, message = "Cena po noći mora biti pozitivna vrednost")
    private double cenaPoNoci;

    @NotBlank(message = "URL slike je obavezan")
    private String slikaUrl;
}
