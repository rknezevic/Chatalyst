package com.example.Chatalyst.service.impl;
/*

import com.example.Chatalyst.model.Demographic;

import com.example.Chatalyst.model.dto.DemographicDTO;
import com.example.Chatalyst.repository.DemographicRepository;

import com.example.Chatalyst.service.DemographicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemographicServiceImpl implements DemographicService {
    private final DemographicRepository demographicRepository;

    @Override
    public List<Demographic> getDemographices() {
        return demographicRepository.findAll();
    }

    @Override
    public Demographic createDemographic(DemographicDTO dto) {
        Demographic demographic = new Demographic();
        demographic.setId(dto.getId());
        demographic.setLongitude(dto.getLongitude());
        demographic.setLatitude(dto.getLatitude());
        demographic.setState(dto.getState());
        demographic.setCountry(dto.getCountry());
        demographic.setStreetAdress(dto.getStreetdemographic());
        demographicRepository.save(demographic);
        return demographic;
    }

    @Override
    public Demographic extendDemographic(DemographicDTO dto) {
        return null;
    }



    @Override
    public DemographicDTO getById(Long id) {
        Optional<Demographic> demographicOptional = demographicRepository.findById(id);
        if (demographicOptional.isEmpty()) return null;
        Demographic demographic = demographicOptional.get();

        return DemographicDTO.todemographicDTO(demographic);
    }
}
*/