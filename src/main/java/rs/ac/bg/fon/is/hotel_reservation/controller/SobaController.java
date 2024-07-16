package rs.ac.bg.fon.is.hotel_reservation.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.service.SobaService;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000") // Prilagodi port React aplikacije
@RequestMapping("/api/sobe")
public class SobaController {
    @Autowired
    private SobaService sobaService;

    @GetMapping
    public List<Soba> getAllSobe() {
        return sobaService.getAllSobe();
    }

    @GetMapping("/{id}")
    public Soba getSobaById(@PathVariable Long id) {
        return sobaService.getSobaById(id);
    }

    @PostMapping
    public Soba saveSoba(@RequestBody Soba soba) {
        return sobaService.saveSoba(soba);
    }

    @DeleteMapping("/{id}")
    public void deleteSoba(@PathVariable Long id) {
        sobaService.deleteSoba(id);
    }
}
