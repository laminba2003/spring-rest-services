package com.spring.training.controller;

import com.spring.training.model.Country;
import com.spring.training.service.CountryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("countries")
@AllArgsConstructor
@Slf4j
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<Country> getCountries() {
        log.debug("returning the list of countries");
        return countryService.getCountries();
    }

    @GetMapping(path="{name}")
    public Country getCountry(@PathVariable("name") String name) {
        log.debug("returning the country with name = {}", name);
        return countryService.getCountry(name);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Country createCountry(@Valid @RequestBody Country country) {
        log.debug("creating country with values = {}", country);
        return countryService.createCountry(country);
    }

    @PutMapping("{id}")
    public Country updateCountry(@PathVariable("id") Long id, @Valid @RequestBody Country country) {
        log.debug("updating country with values = {}", country);
        return countryService.updateCountry(country);
    }

}