package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.GostDTO;
import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.repository.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

import java.security.SecureRandom;
import java.util.Base64;
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

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RezervacijaDTO createReservation(RezervacijaDTO rezervacijaDTO) {
        // Validacija podataka
        if (rezervacijaDTO.getDatumPocetka().isAfter(rezervacijaDTO.getDatumZavrsetka())) {
            throw new RuntimeException("Datum početka rezervacije mora biti pre datuma završetka.");
        }

        // Provera dostupnosti sobe
        Soba soba = sobaRepository.findById(rezervacijaDTO.getSobaId())
                .orElseThrow(() -> new RuntimeException("Soba not found"));

        boolean isAvailable = rezervacijaRepository.findBySobaAndDatumPocetkaLessThanEqualAndDatumZavrsetkaGreaterThanEqual(
                soba, rezervacijaDTO.getDatumZavrsetka(), rezervacijaDTO.getDatumPocetka()).isEmpty();

        if (!isAvailable) {
            throw new RuntimeException("Soba nije dostupna u zadatom periodu.");
        }

        // Provera promo koda i email-a
        if (rezervacijaDTO.getPromoKod() != null && !rezervacijaDTO.getPromoKod().isEmpty()) {
            Rezervacija existingRezervacija = rezervacijaRepository.findByPromoKodAndAktivna(rezervacijaDTO.getPromoKod(), true);
            if (existingRezervacija != null) {
                if (existingRezervacija.getEmail().equals(rezervacijaDTO.getEmail())) {
                    throw new RuntimeException("Promo kod ne može biti korišćen za isti email.");
                } else {
                    // Postavljanje aktivne rezervacije na false
                    existingRezervacija.setAktivna(false);
                    rezervacijaRepository.save(existingRezervacija);
                }
            }
        }

        // Kreiranje rezervacije
        Rezervacija rezervacija = modelMapper.map(rezervacijaDTO, Rezervacija.class);
        rezervacija.setSoba(soba);
        rezervacija.setToken(generateRandomToken());
        rezervacija.setAktivna(true);

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);
        return modelMapper.map(savedRezervacija, RezervacijaDTO.class);
    }

    private String generateRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    @Override
    public RezervacijaDTO getReservationByEmailAndToken(String email, String token) {
        Rezervacija rezervacija = rezervacijaRepository.findByEmailAndToken(email, token)
                .orElseThrow(() -> new RuntimeException("Rezervacija not found"));
        return modelMapper.map(rezervacija, RezervacijaDTO.class);
    }

    @Override
    public void cancelReservation(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new RuntimeException("Rezervacija not found"));
        rezervacijaRepository.delete(rezervacija);
    }
}
