package rs.ac.bg.fon.is.hotel_reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;

@Repository
public interface GostRepository extends JpaRepository<Gost, Long> {
}
