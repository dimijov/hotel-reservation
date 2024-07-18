package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.GostDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.repository.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.GostService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GostServiceImpl implements GostService {

    @Autowired
    private GostRepository gostRepository;

    @Autowired
    private RezervacijaRepository rezervacijaRepository;

    @Override
    public GostDTO createGuest(GostDTO gostDTO) {
        Rezervacija rezervacija = rezervacijaRepository.findById(gostDTO.getRezervacijaId())
                .orElseThrow(() -> new RuntimeException("Rezervacija ne postoji"));

        Gost gost = new Gost();
        gost.setIme(gostDTO.getIme());
        gost.setPrezime(gostDTO.getPrezime());
        gost.setRezervacija(rezervacija);

        Gost savedGost = gostRepository.save(gost);
        return convertToDTO(savedGost);
    }

    @Override
    public GostDTO getGuestById(Long id) {
        Gost gost = gostRepository.findById(id).orElseThrow(() -> new RuntimeException("Gost nije pronađen"));
        return convertToDTO(gost);
    }

    @Override
    public List<GostDTO> getAllGuests() {
        List<Gost> gosti = gostRepository.findAll();
        return gosti.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private GostDTO convertToDTO(Gost gost) {
        GostDTO gostDTO = new GostDTO();
        gostDTO.setId(gost.getId());
        gostDTO.setIme(gost.getIme());
        gostDTO.setPrezime(gost.getPrezime());
        gostDTO.setRezervacijaId(gost.getRezervacija().getId());
        return gostDTO;
    }
}