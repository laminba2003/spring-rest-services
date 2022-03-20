package com.spring.training.service;

import com.spring.training.entity.CountryEntity;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.exception.RequestException;
import com.spring.training.model.Country;
import com.spring.training.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryRepository repository;

    public Country getCountry(String name) {
        return repository.findByNameIgnoreCase(name).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", name)))
                .toCountry();
    }

    public List<Country> getCountries() {
        return repository.findAll().stream()
                .map(entity -> entity.toCountry())
                .collect(Collectors.toList());
    }

    public Country createCountry(Country country) {
        repository.findByNameIgnoreCase(country.getName())
                .ifPresent(entity -> {
                    throw new RequestException(String.format("the country with name %s is already created", entity.getName()),
                            HttpStatus.CONFLICT);
                });
       return repository.save(CountryEntity.fromCountry(country))
                .toCountry();
    }
}