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
        return ResponseEntity.ok(rezervacijaService.createReservation(rezervacijaDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RezervacijaDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(rezervacijaService.getReservationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        rezervacijaService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
