package rs.ac.bg.fon.is.hotel_reservation.service;

import rs.ac.bg.fon.is.hotel_reservation.dto.GostDTO;

import java.util.List;

public interface GostService {
    GostDTO createGuest(GostDTO gostDTO);
    GostDTO getGuestById(Long id);
    List<GostDTO> getAllGuests();
}
