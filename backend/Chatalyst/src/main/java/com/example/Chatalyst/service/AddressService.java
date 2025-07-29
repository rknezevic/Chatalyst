package com.example.Chatalyst.service;

import com.example.Chatalyst.model.Address;
import com.example.Chatalyst.model.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    List<Address> getAddresses();
    Address createAddress(AddressDTO dto);

    Address extendAddress(AddressDTO dto);

    String returnAddress(AddressDTO dto);

}


