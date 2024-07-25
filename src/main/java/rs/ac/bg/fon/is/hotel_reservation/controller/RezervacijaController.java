package rs.ac.bg.fon.is.hotel_reservation.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

@RestController
@RequestMapping("/api/rezervacije")
@Validated
public class RezervacijaController {

    @Autowired
    private RezervacijaService rezervacijaService;

    @PostMapping
    public ResponseEntity<RezervacijaDTO> createReservation(@Valid @RequestBody RezervacijaDTO rezervacijaDTO) {
        RezervacijaDTO createdReservation = rezervacijaService.createReservation(rezervacijaDTO);
        return ResponseEntity.ok(createdReservation);
    }

    @GetMapping
    public ResponseEntity<RezervacijaDTO> getReservationByEmailAndToken(
            @RequestParam String email, @RequestParam String token) {
        RezervacijaDTO reservation = rezervacijaService.getReservationByEmailAndToken(email, token);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        String message = rezervacijaService.cancelReservation(id);
        return ResponseEntity.ok(message);
    }
}