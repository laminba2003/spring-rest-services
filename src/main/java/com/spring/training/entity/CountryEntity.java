package com.spring.training.entity;

import com.spring.training.model.Country;
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

    public Country toCountry() {
        return new Country(name, capital, population);
    }

    public static CountryEntity fromCountry(Country country) {
        CountryEntity entity = new CountryEntity();
        entity.setName(country.getName());
        entity.setCapital(country.getCapital());
        entity.setPopulation(country.getPopulation());
        return entity;
    }

}