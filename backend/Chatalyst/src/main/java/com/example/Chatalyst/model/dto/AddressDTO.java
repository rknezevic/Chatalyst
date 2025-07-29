package com.example.Chatalyst.model.dto;

import com.example.Chatalyst.model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private Long id;

    private Double latitude;

    private Double longitude;

    private String streetAddress;

    private String city;

    private String state;

    private String country;


    public static Address toAddressEntity(AddressDTO dto){
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setState(dto.getState());
        return address;
    }

    public static AddressDTO toAddressDTO(Address address){
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setLatitude(address.getLatitude());
        addressDTO.setLongitude(address.getLongitude());
        addressDTO.setState(address.getState());
        return addressDTO;
    }

}
