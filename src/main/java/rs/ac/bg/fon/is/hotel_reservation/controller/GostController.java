package rs.ac.bg.fon.is.hotel_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.is.hotel_reservation.dto.GostDTO;
import rs.ac.bg.fon.is.hotel_reservation.service.GostService;

import java.util.List;


@RestController
@RequestMapping("/api/gosti")

public class GostController {

    @Autowired
    private GostService gostService;

    @PostMapping
    public ResponseEntity<GostDTO> createGuest(@RequestBody GostDTO gostDTO) {
        return ResponseEntity.ok(gostService.createGuest(gostDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GostDTO> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(gostService.getGuestById(id));
    }

    @GetMapping
    public ResponseEntity<List<GostDTO>> getAllGuests() {
        return ResponseEntity.ok(gostService.getAllGuests());
    }
}