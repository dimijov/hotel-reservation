package rs.ac.bg.fon.is.hotel_reservation.controller;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.is.hotel_reservation.dto.KreirajRezervacijuRequest;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rezervacije")
public class RezervacijaController {
    @Autowired
    private RezervacijaService rezervacijaService;

    @PostMapping("/kreiraj")
    public Rezervacija kreirajRezervaciju(@Valid @RequestBody KreirajRezervacijuRequest request) {
        return rezervacijaService.kreirajRezervaciju(request);
    }

    @GetMapping("/{email}/{token}")
    public Rezervacija dobaviRezervaciju(@PathVariable String email, @PathVariable String token) {
        return rezervacijaService.dobaviRezervaciju(email, token);
    }

    @DeleteMapping("/otkazi/{rezervacijaId}")
    public void otkaziRezervaciju(@PathVariable Long rezervacijaId) {
        rezervacijaService.otkaziRezervaciju(rezervacijaId);
    }
}
