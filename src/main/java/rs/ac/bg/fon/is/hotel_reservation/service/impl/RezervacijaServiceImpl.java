package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.repository.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

import java.util.stream.Collectors;

@Service
public class RezervacijaServiceImpl implements RezervacijaService {

    @Autowired
    private RezervacijaRepository rezervacijaRepository;

    @Autowired
    private SobaRepository sobaRepository;

    @Autowired
    private GostRepository gostRepository;

    @Override
    public RezervacijaDTO createReservation(RezervacijaDTO rezervacijaDTO) {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setEmail(rezervacijaDTO.getEmail());
        rezervacija.setDatumPocetka(rezervacijaDTO.getDatumPocetka());
        rezervacija.setDatumZavrsetka(rezervacijaDTO.getDatumZavrsetka());
        rezervacija.setPromoKod(rezervacijaDTO.getPromoKod());
        rezervacija.setSoba(sobaRepository.findById(rezervacijaDTO.getSobaId()).orElseThrow(() -> new RuntimeException("Soba not found")));
        rezervacija.setGosti(rezervacijaDTO.getGostiId().stream()
                .map(id -> gostRepository.findById(id).orElseThrow(() -> new RuntimeException("Gost not found")))
                .collect(Collectors.toList()));
        rezervacija.setUkupnaCena(rezervacijaDTO.getUkupnaCena());
        rezervacija.setAktivna(rezervacijaDTO.isAktivna());

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);
        return convertToDTO(savedRezervacija);
    }

    @Override
    public RezervacijaDTO getReservationById(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new RuntimeException("Rezervacija not found"));
        return convertToDTO(rezervacija);
    }

    @Override
    public void cancelReservation(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new RuntimeException("Rezervacija not found"));
        rezervacija.setAktivna(false);
        rezervacijaRepository.save(rezervacija);
    }

    private RezervacijaDTO convertToDTO(Rezervacija rezervacija) {
        RezervacijaDTO rezervacijaDTO = new RezervacijaDTO();
        rezervacijaDTO.setId(rezervacija.getId());
        rezervacijaDTO.setEmail(rezervacija.getEmail());
        rezervacijaDTO.setDatumPocetka(rezervacija.getDatumPocetka());
        rezervacijaDTO.setDatumZavrsetka(rezervacija.getDatumZavrsetka());
        rezervacijaDTO.setPromoKod(rezervacija.getPromoKod());
        rezervacijaDTO.setSobaId(rezervacija.getSoba().getId());
        rezervacijaDTO.setGostiId(rezervacija.getGosti().stream().map(Gost::getId).collect(Collectors.toList()));
        rezervacijaDTO.setUkupnaCena(rezervacija.getUkupnaCena());
        rezervacijaDTO.setAktivna(rezervacija.isAktivna());
        return rezervacijaDTO;
    }
}
