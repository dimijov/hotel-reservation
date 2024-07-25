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
import rs.ac.bg.fon.is.hotel_reservation.dao.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.dao.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.dao.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.RezervacijaService;

import java.util.Random;
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
    @Transactional
    public RezervacijaDTO createReservation(RezervacijaDTO rezervacijaDTO) {
        if (rezervacijaDTO.getDatumPocetka().isAfter(rezervacijaDTO.getDatumZavrsetka())) {
            throw new BadRequestException("Datum početka rezervacije mora biti pre datuma završetka.");
        }

        Soba soba = sobaRepository.findById(rezervacijaDTO.getSoba().getId())
                .orElseThrow(() -> new NotFoundException("Soba not found"));

        boolean isAvailable = rezervacijaRepository.findBySobaAndDatumPocetkaLessThanEqualAndDatumZavrsetkaGreaterThanEqual(
                soba, rezervacijaDTO.getDatumZavrsetka(), rezervacijaDTO.getDatumPocetka()).isEmpty();

        if (!isAvailable) {
            throw new BadRequestException("Soba nije dostupna u zadatom periodu.");
        }

        Rezervacija rezervacija = modelMapper.map(rezervacijaDTO, Rezervacija.class);
        rezervacija.setSoba(soba);


        if (rezervacijaDTO.getPromoKod() != null && !rezervacijaDTO.getPromoKod().isEmpty()) {
            Rezervacija existingRezervacija = rezervacijaRepository.findByPromoKodAndAktivna(rezervacijaDTO.getPromoKod(), true);
            if (existingRezervacija != null) {
                rezervacija.setUkupnaCena(calculateDiscountedPrice(rezervacija.getSoba().getCenaPoNoci(),existingRezervacija.getPopust()));
                existingRezervacija.setAktivna(false);
                rezervacijaRepository.save(existingRezervacija);
                rezervacija.setAktivna(true);
            } else {
                throw new BadRequestException("Promo kod nije aktivan ili ne postoji.");
            }
        } else {
            rezervacija.setPopust(0); // No discount if no promo code is provided
        }

        if(!rezervacija.isAktivna()) {
            rezervacija.setUkupnaCena(calculateDiscountedPrice(rezervacija.getSoba().getCenaPoNoci(),rezervacija.getPopust()));
            rezervacija.setAktivna(true);
        }

        rezervacija.setToken(UUID.randomUUID().toString());
        rezervacija.setPopust(generateRandomPopust());
        rezervacija.setPromoKod(generatePromoKod());

        for (Gost gost : rezervacija.getGosti()) {
            gost.setRezervacija(rezervacija);
        }

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);

        RezervacijaDTO responseDTO = modelMapper.map(savedRezervacija, RezervacijaDTO.class);
        return responseDTO;
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
    public void cancelReservation(Long id) {
        Rezervacija rezervacija = rezervacijaRepository.findById(id).orElseThrow(() -> new NotFoundException("Rezervacija not found"));
        rezervacijaRepository.delete(rezervacija);
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