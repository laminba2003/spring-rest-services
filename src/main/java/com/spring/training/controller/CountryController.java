package com.spring.training.controller;

import com.spring.training.annotation.IsAdmin;
import com.spring.training.domain.Country;
import com.spring.training.domain.User;
import com.spring.training.service.CountryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

    @GetMapping("{name}")
    public Country getCountry(@PathVariable("name") String name) {
        log.debug("returning the country with name = {}", name);
        return countryService.getCountry(name);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @IsAdmin
    public Country createCountry(Authentication authentication, @Valid @RequestBody Country country) {
        User user = (User) authentication.getPrincipal();
        log.debug("creating country with values = {} and user : {}", country, user.getEmail());
        return countryService.createCountry(country);
    }

    @PutMapping("{name}")
    @IsAdmin
    public Country updateCountry(Authentication authentication, @PathVariable("name") String name, @Valid @RequestBody Country country) {
        User user = (User) authentication.getPrincipal();
        log.debug("updating country with name = {}, values = {} and user : {}", name, country, user.getEmail());
        return countryService.updateCountry(name, country);
    }

    @DeleteMapping("{name}")
    @IsAdmin
    public void deleteCountry(Authentication authentication, @PathVariable("name") String name) {
        User user = (User) authentication.getPrincipal();
        log.debug("deleting country with name = {} and user : {}", name, user.getEmail());
        countryService.deleteCountry(name);
    }

}