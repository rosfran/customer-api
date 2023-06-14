package com.hr.management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-address"
    )

    @SequenceGenerator(
            name = "sequence-address",
            sequenceName = "sequence_address",
            allocationSize = 5
    )
    private Long id;

    public Address(String streetName ) {
        this.streetName = streetName;
    }

    public Address(String streetName, Integer number, String city, String country ) {
        this.streetName = streetName;
        this.city = city;

        this.country = country;

        this.number = number;
    }

    @Column(length = 300, nullable = true, unique = true)
    private String streetName;

    @Column(length = 300, nullable = false, unique = true)
    private String city;

    @Column(length = 300, nullable = false, unique = true)
    private String country;

    @Column(nullable = false)
    private Integer number;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

}
