package com.spring.training;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.training.controller.CountryController;
import com.spring.training.model.Country;
import com.spring.training.service.CountryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets", uriPort = 9090)
public class CountryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCountries() throws Exception {
        List<Country> countries = getCountries();
        given(countryService.getCountries()).willReturn(countries);
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(countries.get(0).getId()))
                .andExpect(jsonPath("$.[0].name").value(countries.get(0).getName()))
                .andExpect(jsonPath("$.[0].capital").value(countries.get(0).getCapital()))
                .andExpect(jsonPath("$.[0].population").value(countries.get(0).getPopulation()))
        .andDo(document("getCountries"));
    }

    @Test
    public void testGetCountry() throws Exception {
        Country country = getCountry();
        given(countryService.getCountry(country.getName())).willReturn(country);
        mockMvc.perform(get("/countries/{name}", country.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(country.getId()))
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(jsonPath("$.capital").value(country.getCapital()))
                .andExpect(jsonPath("$.population").value(country.getPopulation()))
                .andDo(document("getCountry"));
    }

    @Test
    public void testCreateCountry() throws Exception {
        Country country = getCountry();
        given(countryService.createCountry(country)).willReturn(country);
         mockMvc.perform(post("/countries")
                .content(objectMapper.writeValueAsString(country))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(country.getId()))
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(jsonPath("$.capital").value(country.getCapital()))
                .andExpect(jsonPath("$.population").value(country.getPopulation()))
                .andDo(document("createCountry"));
    }

    @Test
    public void testUpdateCountry() throws Exception {
        Country country = getCountry();
        given(countryService.updateCountry(country)).willReturn(country);
        mockMvc.perform(put("/countries/{id}", country.getId())
                .content(objectMapper.writeValueAsString(country))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(country.getId()))
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(jsonPath("$.capital").value(country.getCapital()))
                .andExpect(jsonPath("$.population").value(country.getPopulation()))
                .andDo(document("updateCountry"));
    }

    private List<Country> getCountries() {
        return Arrays.asList(getCountry());
    }

    private Country getCountry() {
        return new Country(1L, "France", "Paris", 1223333677);
    }

}