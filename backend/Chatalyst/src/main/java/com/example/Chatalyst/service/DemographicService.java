package com.example.Chatalyst.service;

import com.example.Chatalyst.model.Address;
import com.example.Chatalyst.model.Demographic;
import com.example.Chatalyst.model.dto.AddressDTO;
import com.example.Chatalyst.model.dto.DemographicDTO;

import java.util.List;

public interface DemographicService {

    List<Demographic> getDemographics();
    Demographic createDemographic(DemographicDTO dto);

    Demographic extendDemographic(DemographicDTO dto);


    DemographicDTO getById(Long id);
}
