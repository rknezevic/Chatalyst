package com.example.Chatalyst.model.dto;

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
}
