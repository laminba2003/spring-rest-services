package com.spring.training.mapping;

import com.spring.training.domain.Country;
import com.spring.training.entity.CountryEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CountryMapper {

    Country toCountry(CountryEntity countryEntity);

    CountryEntity fromCountry(Country country);

}
