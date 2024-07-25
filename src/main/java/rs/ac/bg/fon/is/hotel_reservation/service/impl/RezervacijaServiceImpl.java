package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.is.hotel_reservation.dto.RezervacijaDTO;
import rs.ac.bg.fon.is.hotel_reservation.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation.exception.BadRequestException;
import rs.ac.bg.fon.is.hotel_reservation.exception.NotFoundException;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.dao.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.dao.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class RezervacijaServiceImpl implements RezervacijaService {

    @Autowired
    private RezervacijaRepository rezervacijaRepository;

    @Autowired
    private SobaRepository sobaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public RezervacijaDTO createReservation(RezervacijaDTO rezervacijaDTO) {
        validateReservationDates(rezervacijaDTO);
        Soba soba = findSoba(rezervacijaDTO.getSoba().getId());
        checkAvailability(soba, rezervacijaDTO);

        Rezervacija rezervacija = modelMapper.map(rezervacijaDTO, Rezervacija.class);
        rezervacija.setSoba(soba);
        processPromoKod(rezervacija);

        setReservationParameters(rezervacija);
        rezervacija.setGosti(setGuestsReservation(rezervacija));

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);

        return modelMapper.map(savedRezervacija, RezervacijaDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RezervacijaDTO getReservationByEmailAndToken(String email, String token) {
        Rezervacija rezervacija = rezervacijaRepository.findByEmailAndToken(email, token)
                .orElseThrow(() -> new NotFoundException("Rezervacija not found"));
        RezervacijaDTO rezervacijaDTO = modelMapper.map(rezervacija, RezervacijaDTO.class);
        rezervacijaDTO.setSoba(modelMapper.map(rezervacija.getSoba(), SobaDTO.class));
        rezervacijaDTO.setToken(null);
        return rezervacijaDTO;
    }

    @Override
    @Transactional
    public String cancelReservation(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new NotFoundException("Rezervacija not found"));
        rezervacijaRepository.delete(rezervacija);
        return "Rezervacija je uspešno otkazana";
    }

    private void validateReservationDates(RezervacijaDTO rezervacijaDTO) {
        if (rezervacijaDTO.getDatumPocetka().isAfter(rezervacijaDTO.getDatumZavrsetka())) {
            throw new BadRequestException("Datum početka rezervacije mora biti pre datuma završetka.");
        }
    }

    private Soba findSoba(Long id) {
        return sobaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Soba not found"));
    }

    private void checkAvailability(Soba soba, RezervacijaDTO rezervacijaDTO) {
        boolean isAvailable = rezervacijaRepository.findBySobaAndDatumPocetkaLessThanEqualAndDatumZavrsetkaGreaterThanEqual(
                soba, rezervacijaDTO.getDatumZavrsetka(), rezervacijaDTO.getDatumPocetka()).isEmpty();
        if (!isAvailable) {
            throw new BadRequestException("Soba nije dostupna u zadatom periodu.");
        }
    }

    private void processPromoKod(Rezervacija rezervacija) {
        if (rezervacija.getPromoKod() != null && !rezervacija.getPromoKod().isEmpty()) {
            Rezervacija existingRezervacija = rezervacijaRepository.findByPromoKodAndAktivna(rezervacija.getPromoKod(), true);
            if (existingRezervacija != null) {
                rezervacija.setUkupnaCena(calculateDiscountedPrice(rezervacija.getSoba().getCenaPoNoci(), existingRezervacija.getPopust()));
                existingRezervacija.setAktivna(false);
                rezervacijaRepository.save(existingRezervacija);
                rezervacija.setAktivna(true);
            } else {
                throw new BadRequestException("Promo kod nije aktivan ili ne postoji.");
            }
        } else {
            rezervacija.setPopust(0); // No discount if no promo code is provided
        }
    }

    private void setReservationParameters(Rezervacija rezervacija) {
        if (!rezervacija.isAktivna()) {
            rezervacija.setUkupnaCena(calculateDiscountedPrice(rezervacija.getSoba().getCenaPoNoci(), rezervacija.getPopust()));
            rezervacija.setAktivna(true);
        }
        rezervacija.setToken(UUID.randomUUID().toString());
        rezervacija.setPopust(generateRandomPopust());
        rezervacija.setPromoKod(generatePromoKod());
    }

    private List<Gost> setGuestsReservation(Rezervacija rezervacija) {
        for (Gost gost : rezervacija.getGosti()) {
            gost.setRezervacija(rezervacija);
        }
        return rezervacija.getGosti();
    }

    private double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        return originalPrice - (originalPrice * (discountPercentage / 100));
    }

    private String generatePromoKod() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder promoKod = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            promoKod.append(characters.charAt(random.nextInt(characters.length())));
        }
        return promoKod.toString();
    }

    private double generateRandomPopust() {
        int[] possiblePopusti = {5, 10, 15, 20};
        return possiblePopusti[new Random().nextInt(possiblePopusti.length)];
    }
}