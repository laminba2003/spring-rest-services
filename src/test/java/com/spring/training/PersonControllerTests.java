package com.spring.training;

import com.spring.training.controller.PersonController;
import com.spring.training.model.Country;
import com.spring.training.model.Person;
import com.spring.training.service.PersonService;
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
@WebMvcTest(PersonController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets", uriPort = 9090)
public class PersonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void testGetPersons() throws Exception {
        given(personService.getPersons()).willReturn(getPersons());
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("Mamadou Lamine"))
                .andExpect(jsonPath("$.[0].lastName").value("Ba"))
                .andDo(document("getPersons"));
    }

    @Test
    public void testGetPerson() throws Exception {
        Long id = 1L;
        given(personService.getPerson(id)).willReturn(getPerson());
        mockMvc.perform(get("/persons/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Mamadou Lamine"))
                .andExpect(jsonPath("$.lastName").value("Ba"))
                .andExpect(jsonPath("$.country.id").value(1L))
                .andExpect(jsonPath("$.country.name").value("France"))
                .andExpect(jsonPath("$.country.capital").value("Paris"))
                .andExpect(jsonPath("$.country.population").value(1223333677))
                .andDo(document("getPerson"));
    }

    private List<Person> getPersons() {
        return Arrays.asList(getPerson());
    }

    private Person getPerson() {
        Country country = new Country(1L, "France", "Paris", 1223333677);
        return new Person(1L, "Mamadou Lamine", "Ba", country);
    }

}