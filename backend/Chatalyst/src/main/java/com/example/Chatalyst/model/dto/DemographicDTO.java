package com.example.Chatalyst.model.dto;


import com.example.Chatalyst.model.Customer;
import com.example.Chatalyst.model.Demographic;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DemographicDTO {

    private Long id;

    private Integer income;

    private Boolean hasChildren;

    private Integer lengthOfResidence;

    private String maritalStatus;

    private Integer homeMarketValueMin;

    private Integer homeMarketValueMax;

    private Boolean homeOwner;

    private Boolean collegeDegree;

    private Boolean goodCredit;

    private Long customerId;



    public static Demographic toDemographicEntity(DemographicDTO dto){
        Demographic demographic = new Demographic();
        demographic.setId(dto.getId());
        demographic.setIncome(dto.getIncome());
        demographic.setHasChildren(dto.getHasChildren());
        demographic.setLengthOfResidence(dto.getLengthOfResidence());
        demographic.setMaritalStatus(dto.getMaritalStatus());
        demographic.setHomeMarketValueMin(dto.getHomeMarketValueMin());
        demographic.setHomeMarketValueMax(dto.getHomeMarketValueMax());
        demographic.setHomeOwner(dto.getHomeOwner());
        demographic.setCollegeDegree(dto.getCollegeDegree());
        demographic.setGoodCredit(dto.getGoodCredit());

        if (dto.getCustomerId() != null) { 
            Customer customer = new Customer();
            customer.setId(dto.getId());
            demographic.setCustomer(customer);
        }
        return demographic;
    }

    public static DemographicDTO toDemographicDTO(Demographic demographic){
        DemographicDTO demographicDTO = new DemographicDTO();
        demographicDTO.setId(demographic.getId());
        demographicDTO.setIncome(demographic.getIncome());
        demographicDTO.setHasChildren(demographic.getHasChildren());
        demographicDTO.setLengthOfResidence(demographic.getLengthOfResidence());
        demographicDTO.setMaritalStatus(demographic.getMaritalStatus());
        demographicDTO.setHomeMarketValueMin(demographic.getHomeMarketValueMin());
        demographicDTO.setHomeMarketValueMax(demographic.getHomeMarketValueMax());
        demographicDTO.setHomeOwner(demographic.getHomeOwner());
        demographicDTO.setCollegeDegree(demographic.getCollegeDegree());
        demographicDTO.setGoodCredit(demographic.getGoodCredit());
        demographicDTO.setCustomerId(demographic.getCustomer().getId());
        return demographicDTO;
    }

}
