package rs.ac.bg.fon.is.hotel_reservation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Rezervacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private String token;
    private String promoKod;
    private double ukupnaCena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soba_id")
    @JsonBackReference
    private Soba soba;

    @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Gost> gosti;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(LocalDate datumOd) {
        this.datumOd = datumOd;
    }

    public LocalDate getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(LocalDate datumDo) {
        this.datumDo = datumDo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPromoKod() {
        return promoKod;
    }

    public void setPromoKod(String promoKod) {
        this.promoKod = promoKod;
    }

    public double getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(double ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public Soba getSoba() {
        return soba;
    }

    public void setSoba(Soba soba) {
        this.soba = soba;
    }

    public List<Gost> getGosti() {
        return gosti;
    }

    public void setGosti(List<Gost> gosti) {
        this.gosti = gosti;
    }
}
