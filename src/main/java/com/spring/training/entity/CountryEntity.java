package com.spring.training.entity;

import com.spring.training.model.Country;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "countries")
public class CountryEntity {

    @Id
    private Long id;
    private String name;
    private String capital;
    private int population;

    public Country toCountry() {
        return new Country(id, name, capital, population);
    }

}