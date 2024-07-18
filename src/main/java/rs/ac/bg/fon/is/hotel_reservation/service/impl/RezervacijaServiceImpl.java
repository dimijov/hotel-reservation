package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.GostDTO;
import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.repository.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

import java.util.List;
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
        rezervacija.setSoba(sobaRepository.findById(rezervacijaDTO.getSobaId()).orElseThrow(() -> new RuntimeException("Soba nije pronađena")));
        rezervacija.setUkupnaCena(rezervacijaDTO.getUkupnaCena());
        rezervacija.setAktivna(rezervacijaDTO.isAktivna());

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);

        List<Gost> gosti = rezervacijaDTO.getGosti().stream().map(gostDTO -> {
            Gost gost = new Gost();
            gost.setIme(gostDTO.getIme());
            gost.setPrezime(gostDTO.getPrezime());
            gost.setRezervacija(savedRezervacija);
            return gostRepository.save(gost);
        }).collect(Collectors.toList());

        savedRezervacija.setGosti(gosti);

        return convertToDTO(savedRezervacija);
    }

    @Override
    public RezervacijaDTO getReservationById(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new RuntimeException("Rezervacija nije pronađena"));
        return convertToDTO(rezervacija);
    }

    @Override
    public void cancelReservation(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new RuntimeException("Rezervacija nije pronađena"));
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
        rezervacijaDTO.setUkupnaCena(rezervacija.getUkupnaCena());
        rezervacijaDTO.setAktivna(rezervacija.isAktivna());
        rezervacijaDTO.setGosti(rezervacija.getGosti().stream().map(gost -> {
            GostDTO gostDTO = new GostDTO();
            gostDTO.setId(gost.getId());
            gostDTO.setIme(gost.getIme());
            gostDTO.setPrezime(gost.getPrezime());
            gostDTO.setRezervacijaId(rezervacija.getId());
            return gostDTO;
        }).collect(Collectors.toList()));
        return rezervacijaDTO;
    }
}
