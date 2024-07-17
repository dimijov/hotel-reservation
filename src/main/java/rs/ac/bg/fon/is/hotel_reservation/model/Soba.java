package rs.ac.bg.fon.is.hotel_reservation.model;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
public class Soba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private int kapacitet;
    private String opis;
    private double cenaPoNoci;
    private String slikaUrl;
}
