package com.spring.training.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.training.BaseClass;
import com.spring.training.domain.Country;
import com.spring.training.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets/countries", uriPort = 9090)
public class CountryControllerTests extends BaseClass {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCountries() throws Exception {
        List<Country> countries = Collections.singletonList(getCountry());
        given(countryService.getCountries()).willReturn(countries);
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name").value(countries.get(0).getName()))
                .andExpect(jsonPath("$.[0].capital").value(countries.get(0).getCapital()))
                .andExpect(jsonPath("$.[0].population").value(countries.get(0).getPopulation()))
                .andDo(document("getCountries"))
                .andDo(document("getCountries", SpringCloudContractRestDocs.dslContract()));
    }

    @Test
    public void testGetCountry() throws Exception {
        Country country = getCountry();
        given(countryService.getCountry(country.getName())).willReturn(country);
        mockMvc.perform(get("/countries/{name}", country.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(jsonPath("$.capital").value(country.getCapital()))
                .andExpect(jsonPath("$.population").value(country.getPopulation()))
                .andDo(document("getCountry"))
                .andDo(document("getCountry", SpringCloudContractRestDocs.dslContract()));
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
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(jsonPath("$.capital").value(country.getCapital()))
                .andExpect(jsonPath("$.population").value(country.getPopulation()))
                .andDo(document("createCountry"))
                .andDo(document("createCountry", SpringCloudContractRestDocs.dslContract()));
    }

    @Test
    public void testUpdateCountry() throws Exception {
        Country country = getCountry();
        given(countryService.updateCountry(country.getName(), country)).willReturn(country);
        mockMvc.perform(put("/countries/{name}", country.getName())
                .content(objectMapper.writeValueAsString(country))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(country.getName()))
                .andExpect(jsonPath("$.capital").value(country.getCapital()))
                .andExpect(jsonPath("$.population").value(country.getPopulation()))
                .andDo(document("updateCountry"))
                .andDo(document("updateCountry", SpringCloudContractRestDocs.dslContract()));
    }

    @Test
    public void testDeleteCountry() throws Exception {
        Country country = getCountry();
        doNothing().when(countryService).deleteCountry(country.getName());
        mockMvc.perform(delete("/countries/{name}", country.getName()))
                .andExpect(status().isOk())
                .andDo(document("deleteCountry"))
                .andDo(document("deleteCountry", SpringCloudContractRestDocs.dslContract()));
    }

}