package rs.ac.bg.fon.is.hotel_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;

public interface GostRepository extends JpaRepository<Gost, Long> {
}
