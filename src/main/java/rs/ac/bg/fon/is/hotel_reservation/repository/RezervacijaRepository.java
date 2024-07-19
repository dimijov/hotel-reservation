package rs.ac.bg.fon.is.hotel_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {
    List<Rezervacija> findBySobaAndDatumPocetkaLessThanEqualAndDatumZavrsetkaGreaterThanEqual(Soba soba, LocalDate datumZavrsetka, LocalDate datumPocetka);
    Optional<Rezervacija> findByEmailAndToken(String email, String token);
    Rezervacija findByPromoKodAndAktivna(String promoKod, boolean aktivna);
}
