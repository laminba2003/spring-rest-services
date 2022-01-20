package com.spring.training.service;

import com.spring.training.model.Country;
import com.spring.training.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CountryService {

    private CountryRepository repository;

    public Country findCountry(String name) {
        return repository.findByNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("country not found")).toCountry();
    }

    public List<Country> getCountries() {
        return repository.findAll().stream().map(entity -> entity.toCountry()).collect(Collectors.toList());
    }

}