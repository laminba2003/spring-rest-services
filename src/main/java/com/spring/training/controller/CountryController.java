package com.spring.training.controller;

import com.spring.training.annotation.IsAdmin;
import com.spring.training.domain.Country;
import com.spring.training.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("countries")
@AllArgsConstructor
public class CountryController {

    CountryService countryService;

    @GetMapping
    @Operation(summary = "getCountries", description = "return the list of countries")
    @ApiResponse(responseCode = "200", description = "countries found successfully")
    public List<Country> getCountries() {
        return countryService.getCountries();
    }

    @GetMapping("{name}")
    @Operation(summary = "getCountry", description = "return a country by its name")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "country found successfully"),
            @ApiResponse(responseCode = "404", description = "country not found")})
    public Country getCountry(@Parameter(description = "country name", required = true) @PathVariable("name") String name) {
        return countryService.getCountry(name);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "createCountry", description = "create a country")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "country created successfully"),
            @ApiResponse(responseCode = "409", description = "country already created")})
    @IsAdmin
    public Country createCountry(@Valid @RequestBody Country country) {
        return countryService.createCountry(country);
    }

    @PutMapping("{name}")
    @Operation(summary = "updateCountry", description = "update a country by its name")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "country updated successfully"),
            @ApiResponse(responseCode = "404", description = "country not found")})
    @IsAdmin
    public Country updateCountry(@Parameter(description = "country name", required = true) @PathVariable("name") String name,
                                 @Valid @RequestBody Country country) {
        return countryService.updateCountry(name, country);
    }

    @DeleteMapping("{name}")
    @Operation(summary = "deleteCountry", description = "delete a country by its name")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "country deleted successfully"),
            @ApiResponse(responseCode = "409", description = "cannot delete country")})
    @IsAdmin
    public void deleteCountry(@Parameter(description = "country name", required = true) @PathVariable("name") String name) {
        countryService.deleteCountry(name);
    }

}