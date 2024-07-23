package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.dao.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.dao.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.dao.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

import java.util.UUID;

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
        if (rezervacijaDTO.getDatumPocetka().isAfter(rezervacijaDTO.getDatumZavrsetka())) {
            throw new RuntimeException("Datum početka rezervacije mora biti pre datuma završetka.");
        }

        Soba soba = sobaRepository.findById(rezervacijaDTO.getSoba().getId())
                .orElseThrow(() -> new RuntimeException("Soba not found"));

        boolean isAvailable = rezervacijaRepository.findBySobaAndDatumPocetkaLessThanEqualAndDatumZavrsetkaGreaterThanEqual(
                soba, rezervacijaDTO.getDatumZavrsetka(), rezervacijaDTO.getDatumPocetka()).isEmpty();

        if (!isAvailable) {
            throw new RuntimeException("Soba nije dostupna u zadatom periodu.");
        }

        Rezervacija rezervacija = modelMapper.map(rezervacijaDTO, Rezervacija.class);
        rezervacija.setSoba(soba);
        rezervacija.setToken(UUID.randomUUID().toString());

        if (rezervacijaDTO.getPromoKod() != null && !rezervacijaDTO.getPromoKod().isEmpty()) {
            Rezervacija existingRezervacija = rezervacijaRepository.findByPromoKodAndAktivna(rezervacijaDTO.getPromoKod(), true);
            if (existingRezervacija != null) {
                rezervacija.setPopust(existingRezervacija.getPopust());
            } else {
                throw new RuntimeException("Promo kod nije aktivan ili ne postoji.");
            }
        } else {
            rezervacija.setPopust(0); // No discount if no promo code is provided
        }

        rezervacija.setUkupnaCena(calculateDiscountedPrice(rezervacijaDTO.getUkupnaCena(), rezervacija.getPopust()));

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);

        for (Gost gost : savedRezervacija.getGosti()) {
            gost.setRezervacija(savedRezervacija);
            gostRepository.save(gost);
        }

        RezervacijaDTO responseDTO = modelMapper.map(savedRezervacija, RezervacijaDTO.class);
        responseDTO.setSoba(modelMapper.map(savedRezervacija.getSoba(), SobaDTO.class));
        return responseDTO;
    }

    private double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        return originalPrice - (originalPrice * (discountPercentage / 100));
    }

    @Override
    public RezervacijaDTO getReservationByEmailAndToken(String email, String token) {
        Rezervacija rezervacija = rezervacijaRepository.findByEmailAndToken(email, token)
                .orElseThrow(() -> new RuntimeException("Rezervacija not found"));
        RezervacijaDTO rezervacijaDTO = modelMapper.map(rezervacija, RezervacijaDTO.class);
        rezervacijaDTO.setSoba(modelMapper.map(rezervacija.getSoba(), SobaDTO.class));
        rezervacijaDTO.setToken(null);
        return rezervacijaDTO;
    }


    @Override
    public void cancelReservation(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new RuntimeException("Rezervacija not found"));
        rezervacijaRepository.delete(rezervacija);
    }
}

