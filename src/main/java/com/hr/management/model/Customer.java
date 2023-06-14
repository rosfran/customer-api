package com.hr.management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"firstName", "lastName"})
public class Customer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-customer"
    )
    @SequenceGenerator(
            name = "sequence-customer",
            sequenceName = "sequence_customer",
            allocationSize = 5
    )
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Date createdAt;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            targetEntity = Address.class,
            orphanRemoval = true
    )
    private Set<Address> addresses;


}
