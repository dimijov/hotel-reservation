package rs.ac.bg.fon.is.hotel_reservation.service;

import rs.ac.bg.fon.is.hotel_reservation.dto.SobaDTO;

import java.util.List;

public interface SobaService {
    List<SobaDTO> getAllAvailableRooms();
    SobaDTO getRoomById(Long id);
}
