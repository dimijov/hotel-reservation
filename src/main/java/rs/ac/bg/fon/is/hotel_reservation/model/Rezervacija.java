package rs.ac.bg.fon.is.hotel_reservation.model;

import lombok.Data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Rezervacija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;
    private String promoKod;
    private String token;

    @ManyToOne
    @JoinColumn(name = "soba_id")
    private Soba soba;

    @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL)
    private List<Gost> gosti;

    private double ukupnaCena;
    private boolean aktivna;
}
