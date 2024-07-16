package rs.ac.bg.fon.is.hotel_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {
    List<Rezervacija> findBySobaAndDatumOdLessThanEqualAndDatumDoGreaterThanEqual(Soba soba, LocalDate datumDo, LocalDate datumOd);
    Optional<Rezervacija> findByEmailAndToken(String email, String token);
}
