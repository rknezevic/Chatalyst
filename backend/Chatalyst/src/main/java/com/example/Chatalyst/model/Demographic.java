package com.example.Chatalyst.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Demographic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "individual_id")
    private Customer customer;
}
