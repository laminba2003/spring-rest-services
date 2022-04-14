package com.spring.training.controller;

import com.spring.training.annotation.IsAdmin;
import com.spring.training.domain.Country;
import com.spring.training.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("countries")
@AllArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<Country> getCountries() {
        return countryService.getCountries();
    }

    @GetMapping("{name}")
    public Country getCountry(@PathVariable("name") String name) {
        return countryService.getCountry(name);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @IsAdmin
    public Country createCountry(@Valid @RequestBody Country country) {
        return countryService.createCountry(country);
    }

    @PutMapping("{name}")
    @IsAdmin
    public Country updateCountry(@PathVariable("name") String name, @Valid @RequestBody Country country) {
        return countryService.updateCountry(name, country);
    }

    @DeleteMapping("{name}")
    @IsAdmin
    public void deleteCountry(@PathVariable("name") String name) {
        countryService.deleteCountry(name);
    }

}