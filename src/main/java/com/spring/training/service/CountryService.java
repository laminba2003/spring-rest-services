package com.spring.training.service;

import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.exception.RequestException;
import com.spring.training.domain.Country;
import com.spring.training.mapping.CountryMapper;
import com.spring.training.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@CacheConfig(cacheNames = "countries")
@AllArgsConstructor
public class CountryService {

    CountryRepository countryRepository;
    CountryMapper countryMapper;
    MessageSource messageSource;

    public List<Country> getCountries() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false)
                .map(countryMapper::toCountry)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#name")
    public Country getCountry(String name) {
        return countryMapper.toCountry(countryRepository.findByNameIgnoreCase(name).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("country.notfound", new Object[]{name},
                        Locale.getDefault()))));
    }

    public Country createCountry(Country country) {
        countryRepository.findByNameIgnoreCase(country.getName())
                .ifPresent(entity -> {
                    throw new RequestException(messageSource.getMessage("country.exists", new Object[]{country.getName()},
                            Locale.getDefault()), HttpStatus.CONFLICT);
                });
        return countryMapper.toCountry(countryRepository.save(countryMapper.fromCountry(country)));
    }

    @CachePut(key = "#name")
    public Country updateCountry(String name, Country country) {
        return countryRepository.findByNameIgnoreCase(name)
                .map(entity -> {
                    country.setName(name);
                    return countryMapper.toCountry(
                            countryRepository.save(countryMapper.fromCountry(country)));
                }).orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("country.notfound", new Object[]{name},
                        Locale.getDefault())));
    }

    @CacheEvict(key = "#name")
    public void deleteCountry(String name) {
        try {
            countryRepository.deleteById(name);
        } catch (Exception e) {
            throw new RequestException(messageSource.getMessage("country.errordeletion", new Object[]{name},
                    Locale.getDefault()),
                    HttpStatus.CONFLICT);
        }
    }

}