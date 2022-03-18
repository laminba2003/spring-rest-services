package com.spring.training.service;

import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.model.Country;
import com.spring.training.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryRepository repository;

    @Cacheable("countries")
    public Country getCountry(String name) {
        return repository.findByNameIgnoreCase(name).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", name)))
                .toCountry();
    }

    @Cacheable("countries")
    public List<Country> getCountries() {
        return repository.findAll().stream().map(entity -> entity.toCountry()).collect(Collectors.toList());
    }

}