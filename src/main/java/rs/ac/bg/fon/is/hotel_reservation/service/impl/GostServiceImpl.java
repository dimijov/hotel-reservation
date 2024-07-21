package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.GostDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;
import rs.ac.bg.fon.is.hotel_reservation.model.Rezervacija;
import rs.ac.bg.fon.is.hotel_reservation.dao.GostRepository;
import rs.ac.bg.fon.is.hotel_reservation.dao.RezervacijaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.GostService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GostServiceImpl implements GostService {

    @Autowired
    private GostRepository gostRepository;

    @Autowired
    private RezervacijaRepository rezervacijaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GostDTO createGuest(GostDTO gostDTO) {
        Rezervacija rezervacija = rezervacijaRepository.findById(gostDTO.getRezervacijaId())
                .orElseThrow(() -> new RuntimeException("Rezervacija ne postoji"));

        Gost gost = modelMapper.map(gostDTO, Gost.class);
        gost.setRezervacija(rezervacija);

        Gost savedGost = gostRepository.save(gost);
        return modelMapper.map(savedGost, GostDTO.class);
    }

    @Override
    public GostDTO getGuestById(Long id) {
        Gost gost = gostRepository.findById(id).orElseThrow(() -> new RuntimeException("Gost nije pronađen"));
        return modelMapper.map(gost, GostDTO.class);
    }

    @Override
    public List<GostDTO> getAllGuests() {
        List<Gost> gosti = gostRepository.findAll();
        return gosti.stream().map(gost -> modelMapper.map(gost, GostDTO.class)).collect(Collectors.toList());
    }
}