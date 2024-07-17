package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.repository.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.SobaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SobaServiceImpl implements SobaService {

    @Autowired
    private SobaRepository sobaRepository;

    @Override
    public List<SobaDTO> getAllAvailableRooms() {
        List<Soba> sobe = sobaRepository.findAll();
        return sobe.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public SobaDTO getRoomById(Long id) {
        Soba soba = sobaRepository.findById(id).orElseThrow(() -> new RuntimeException("Soba not found"));
        return convertToDTO(soba);
    }

    private SobaDTO convertToDTO(Soba soba) {
        SobaDTO sobaDTO = new SobaDTO();
        sobaDTO.setId(soba.getId());
        sobaDTO.setNaziv(soba.getNaziv());
        sobaDTO.setKapacitet(soba.getKapacitet());
        sobaDTO.setOpis(soba.getOpis());
        sobaDTO.setCenaPoNoci(soba.getCenaPoNoci());
        sobaDTO.setSlikaUrl(soba.getSlikaUrl());
        return sobaDTO;
    }
}
