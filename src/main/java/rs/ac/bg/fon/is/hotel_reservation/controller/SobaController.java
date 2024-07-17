package rs.ac.bg.fon.is.hotel_reservation.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.is.hotel_reservation.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation.service.SobaService;


import java.util.List;

@RestController
@RequestMapping("/api/sobe")
public class SobaController {

    @Autowired
    private SobaService sobaService;

    @GetMapping
    public ResponseEntity<List<SobaDTO>> getAllAvailableRooms() {
        return ResponseEntity.ok(sobaService.getAllAvailableRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SobaDTO> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(sobaService.getRoomById(id));
    }
}
