package rs.ac.bg.fon.is.hotel_reservation.dto;

import lombok.Data;

@Data
public class SobaDTO {
    private Long id;
    private String naziv;
    private int kapacitet;
    private String opis;
    private double cenaPoNoci;
    private String slikaUrl;
}
