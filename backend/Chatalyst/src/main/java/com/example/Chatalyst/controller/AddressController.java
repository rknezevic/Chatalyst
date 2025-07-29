package com.example.Chatalyst.controller;

import com.example.Chatalyst.model.Address;
import com.example.Chatalyst.model.dto.AddressDTO;
import com.example.Chatalyst.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;


    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.createAddress(dto));
    }


    @GetMapping("/all")
    public List<Address> findAll(){
        return addressService.getAddresses();
    }


}
