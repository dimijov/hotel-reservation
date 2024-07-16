package rs.ac.bg.fon.is.hotel_reservation.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.KreirajRezervacijuRequest;
import rs.ac.bg.fon.is.hotel_reservation.exception.RezervacijaNotFoundException;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.repository.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.repository.SobaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class RezervacijaService {
    @Autowired
    private RezervacijaRepository rezervacijaRepository;

    @Autowired
    private SobaRepository sobaRepository;

    @Autowired
    private GostRepository gostRepository;

    @Transactional
    public Rezervacija kreirajRezervaciju(KreirajRezervacijuRequest request) {
        Optional<Soba> sobaOptional = sobaRepository.findById(request.getSobaId());
        if (!sobaOptional.isPresent()) {
            throw new IllegalArgumentException("Soba sa datim ID ne postoji.");
        }

        Soba soba = sobaOptional.get();

        // Provera dostupnosti
        List<Rezervacija> postojecaRezervacija = rezervacijaRepository.findBySobaAndDatumOdLessThanEqualAndDatumDoGreaterThanEqual(
                soba, request.getDatumDo(), request.getDatumOd());
        if (!postojecaRezervacija.isEmpty()) {
            throw new IllegalArgumentException("Soba nije dostupna u zadatom periodu.");
        }

        // Provera kapaciteta sobe
        if (soba.getKapacitet() < request.getGosti().size()) {
            throw new IllegalArgumentException("Soba ne može da primi toliko gostiju.");
        }

        // Provera datuma
        if (request.getDatumOd().isAfter(request.getDatumDo())) {
            throw new IllegalArgumentException("Datum od mora biti pre datuma do.");
        }

        // Provera datuma
        LocalDate danas = LocalDate.now();
        if (request.getDatumOd().isBefore(danas.plusDays(1))) {
            throw new IllegalArgumentException("Datum početka rezervacije mora biti sutrašnji dan ili kasnije.");
        }

        // Kreiranje rezervacije
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setEmail(request.getEmail());
        rezervacija.setDatumOd(request.getDatumOd());
        rezervacija.setDatumDo(request.getDatumDo());
        rezervacija.setSoba(soba);
        rezervacija.setToken(UUID.randomUUID().toString());
        rezervacija.setPromoKod(request.getPromoKod());
        rezervacija.setUkupnaCena(izracunajUkupnuCenu(soba.getCenaPoNoci(), request.getDatumOd(), request.getDatumDo(), request.getPromoKod()));

        // Sačuvaj rezervaciju kako bi dobila ID
        rezervacija = rezervacijaRepository.save(rezervacija);

        // Povezivanje gostiju sa rezervacijom i čuvanje gostiju
        for (Gost gost : request.getGosti()) {
            gost.setRezervacija(rezervacija);
            gostRepository.save(gost);
        }

        return rezervacija;
    }

    private double izracunajUkupnuCenu(double cenaPoNoci, LocalDate datumOd, LocalDate datumDo, String promoKod) {
        long brojNoci = datumOd.until(datumDo).getDays();
        double ukupnaCena = cenaPoNoci * brojNoci;

        if (promoKod != null) {
            // Implementiraj logiku za popust na osnovu promo koda
        }

        return ukupnaCena;
    }

    public Rezervacija dobaviRezervaciju(String email, String token) {
        Optional<Rezervacija> rezervacija = rezervacijaRepository.findByEmailAndToken(email, token);
        if (rezervacija.isPresent()) {
            return rezervacija.get();
        } else {
            throw new RezervacijaNotFoundException("Rezervacija sa zadatim emailom i tokenom nije pronađena.");
        }
    }

    @Transactional
    public void otkaziRezervaciju(Long rezervacijaId) {
        Optional<Rezervacija> rezervacijaOptional = rezervacijaRepository.findById(rezervacijaId);
        if (!rezervacijaOptional.isPresent()) {
            throw new IllegalArgumentException("Rezervacija sa datim ID ne postoji.");
        }

        Rezervacija rezervacija = rezervacijaOptional.get();
        if (rezervacija.getDatumOd().minusDays(5).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Rezervaciju je moguće otkazati minimum 5 dana pre početka.");
        }

        rezervacijaRepository.delete(rezervacija);
    }
}
