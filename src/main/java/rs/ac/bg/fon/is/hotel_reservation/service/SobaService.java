package rs.ac.bg.fon.is.hotel_reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.repository.SobaRepository;
import java.util.List;

@Service
public class SobaService {
    @Autowired
    private SobaRepository sobaRepository;

    public List<Soba> getAllSobe() {
        return sobaRepository.findAll();
    }

    public Soba getSobaById(Long id) {
        return sobaRepository.findById(id).orElse(null);
    }

    public Soba saveSoba(Soba soba) {
        return sobaRepository.save(soba);
    }

    public void deleteSoba(Long id) {
        sobaRepository.deleteById(id);
    }
}
