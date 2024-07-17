package rs.ac.bg.fon.is.hotel_reservation.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Rezervacija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private Date datumPocetka;
    private Date datumZavrsetka;
    private String promoKod;

    @ManyToOne
    @JoinColumn(name = "soba_id")
    private Soba soba;

    @ManyToMany
    @JoinTable(
            name = "rezervacija_gost",
            joinColumns = @JoinColumn(name = "rezervacija_id"),
            inverseJoinColumns = @JoinColumn(name = "gost_id")
    )
    private List<Gost> gosti;

    private double ukupnaCena;
    private boolean aktivna;
}
