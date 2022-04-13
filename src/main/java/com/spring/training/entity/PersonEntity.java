package com.spring.training.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name="persons")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @ManyToOne
    private CountryEntity country;

}
