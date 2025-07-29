package com.example.Chatalyst.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double currAnnAmt;

    private Integer daysTenure;

    private LocalDate custOrigDate;

    private Integer ageInYears;

    private LocalDate dateOfBirth;

    private String socialSecurityNumber;

    private LocalDate acctSuspdDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "adress_id")
    private Adress adress;

}
