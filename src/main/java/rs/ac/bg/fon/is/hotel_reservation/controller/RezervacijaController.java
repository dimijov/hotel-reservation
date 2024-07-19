package rs.ac.bg.fon.is.hotel_reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

@RestController
@RequestMapping("/api/rezervacije")
public class RezervacijaController {

    @Autowired
    private RezervacijaService rezervacijaService;

    @PostMapping
    public ResponseEntity<RezervacijaDTO> createReservation(@RequestBody RezervacijaDTO rezervacijaDTO) {
        RezervacijaDTO createdReservation = rezervacijaService.createReservation(rezervacijaDTO);
        return ResponseEntity.ok(createdReservation);
    }

  /*
    @GetMapping("/{id}")
    public ResponseEntity<RezervacijaDTO> getReservationById(@PathVariable Long id) {
        RezervacijaDTO reservation = rezervacijaService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }
  */

    @GetMapping
    public ResponseEntity<RezervacijaDTO> getReservationByEmailAndToken(
            @RequestParam String email, @RequestParam String token) {
        RezervacijaDTO reservation = rezervacijaService.getReservationByEmailAndToken(email, token);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        rezervacijaService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}