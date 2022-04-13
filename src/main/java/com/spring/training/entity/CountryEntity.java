package com.spring.training.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "countries")
public class CountryEntity {

    @Id
    private String name;
    private String capital;
    private int population;

}