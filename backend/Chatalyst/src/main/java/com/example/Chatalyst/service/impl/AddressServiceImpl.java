package com.example.Chatalyst.service.impl;

import com.example.Chatalyst.model.Address;
import com.example.Chatalyst.model.dto.AddressDTO;
import com.example.Chatalyst.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.Chatalyst.repository.AddressRepository;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public List<Address> getAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address createAddress(AddressDTO dto) {
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setLongitude(dto.getLongitude());
        address.setLatitude(dto.getLatitude());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setStreetAdress(dto.getStreetAddress());
        addressRepository.save(address);
        return address;
    }

    @Override
    public Address extendAddress(AddressDTO dto) {
        return null;
    }

    @Override
    public String returnAddress(AddressDTO dto) {
        return "";
    }
}
