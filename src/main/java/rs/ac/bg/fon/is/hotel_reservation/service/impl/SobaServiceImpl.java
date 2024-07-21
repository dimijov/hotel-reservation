package rs.ac.bg.fon.is.hotel_reservation.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.is.hotel_reservation.dto.SobaDTO;
import rs.ac.bg.fon.is.hotel_reservation.model.Soba;
import rs.ac.bg.fon.is.hotel_reservation.dao.SobaRepository;
import rs.ac.bg.fon.is.hotel_reservation.service.SobaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SobaServiceImpl implements SobaService {

    @Autowired
    private SobaRepository sobaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SobaDTO> getAllAvailableRooms() {
        List<Soba> sobe = sobaRepository.findAll();
        return sobe.stream().map(soba -> modelMapper.map(soba, SobaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public SobaDTO getRoomById(Long id) {
        Soba soba = sobaRepository.findById(id).orElseThrow(() -> new RuntimeException("Soba not found"));
        return modelMapper.map(soba, SobaDTO.class);
    }
}