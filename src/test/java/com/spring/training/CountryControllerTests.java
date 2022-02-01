package com.spring.training;

import com.spring.training.controller.CountryController;
import com.spring.training.model.Country;
import com.spring.training.service.CountryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets", uriPort = 9090)
public class CountryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void testGetCountries() throws Exception {
        given(countryService.getCountries()).willReturn(getCountries());
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("France"))
                .andExpect(jsonPath("$.[0].capital").value("Paris"))
                .andExpect(jsonPath("$.[0].population").value(1223333677))
        .andDo(document("getCountries"));
    }

    @Test
    public void testGetCountry() throws Exception {
        String name = "France";
        given(countryService.getCountry(name)).willReturn(getCountry());
        mockMvc.perform(get("/countries/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("France"))
                .andExpect(jsonPath("$.capital").value("Paris"))
                .andExpect(jsonPath("$.population").value(1223333677))
                .andDo(document("getCountry"));
    }

    private List<Country> getCountries() {
        return Arrays.asList(getCountry());
    }

    private Country getCountry() {
        return new Country(1L, "France", "Paris", 1223333677);
    }

}