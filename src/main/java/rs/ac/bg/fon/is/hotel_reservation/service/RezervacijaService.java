package rs.ac.bg.fon.is.hotel_reservation.service;

import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;

public interface RezervacijaService {
    RezervacijaDTO createReservation(RezervacijaDTO rezervacijaDTO);
    //RezervacijaDTO getReservationById(Long id);
    void cancelReservation(Long id);
    RezervacijaDTO getReservationByEmailAndToken(String email, String token);
}
