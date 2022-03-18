package com.spring.training.controller;

import com.spring.training.model.Country;
import com.spring.training.service.CountryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("countries")
@AllArgsConstructor
@Slf4j
public class CountryController {

    private final CountryService countryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Country> getCountries() {
        log.debug("returning the list of countries");
        return countryService.getCountries();
    }

    @GetMapping(path="{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Country getCountry(@PathVariable("name") String name) {
        log.debug("returning the country with name = {}", name);
        return countryService.getCountry(name);
    }

}