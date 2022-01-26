package com.spring.training;

import com.spring.training.controller.CountryController;
import com.spring.training.model.Country;
import com.spring.training.service.CountryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
public class WebLayerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void testGetCountries() throws Exception {
        given(countryService.getCountries()).willReturn(getCountries());
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("France"))
                .andExpect(jsonPath("$.[0].capital").value("Paris"))
                .andExpect(jsonPath("$.[0].population").value(1223333677));
    }

    private List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();
        Country country = new Country();
        country.setName("France");
        country.setCapital("Paris");
        country.setPopulation(1223333677);
        countries.add(country);
        return countries;
    }

}