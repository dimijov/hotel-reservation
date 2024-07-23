package rs.ac.bg.fon.is.hotel_reservation.model;

import lombok.Data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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
    private double popust;

    public Rezervacija() {
        this.promoKod = generatePromoKod();
        this.aktivna = true;
        this.popust = generateRandomPopust(); // Initialize with a random discount
    }

    private String generatePromoKod() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder promoKod = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            promoKod.append(characters.charAt(random.nextInt(characters.length())));
        }
        return promoKod.toString();
    }

    private double generateRandomPopust() {
        int[] possiblePopusti = {5, 10, 15, 20};
        return possiblePopusti[new Random().nextInt(possiblePopusti.length)];
    }
}

