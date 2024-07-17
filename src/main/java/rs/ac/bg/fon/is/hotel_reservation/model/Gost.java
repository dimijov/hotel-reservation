package rs.ac.bg.fon.is.hotel_reservation.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Gost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;
    private String prezime;
}