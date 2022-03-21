package com.spring.training;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(PersonController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets", uriPort = 9090)
public class PersonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetPersons() throws Exception {
        List<Person> persons = getPersons();
        given(personService.getPersons()).willReturn(getPersons());
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(persons.get(0).getId()))
                .andExpect(jsonPath("$.[0].firstName").value(persons.get(0).getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(persons.get(0).getLastName()))
                .andExpect(jsonPath("$.[0].country.name").value(persons.get(0).getCountry().getName()))
                .andExpect(jsonPath("$.[0].country.capital").value(persons.get(0).getCountry().getCapital()))
                .andExpect(jsonPath("$.[0].country.population").value(persons.get(0).getCountry().getPopulation()))
                .andDo(document("getPersons"));
    }

    @Test
    public void testGetPerson() throws Exception {
        Person person = getPerson();
        given(personService.getPerson(person.getId())).willReturn(person);
        mockMvc.perform(get("/persons/{id}", person.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                .andExpect(jsonPath("$.country.name").value(person.getCountry().getName()))
                .andExpect(jsonPath("$.country.capital").value(person.getCountry().getCapital()))
                .andExpect(jsonPath("$.country.population").value(person.getCountry().getPopulation()))
                .andDo(document("getPerson"));
    }

    @Test
    public void testCreatePerson() throws Exception {
        Person person = getPerson();
        given(personService.createPerson(person)).willReturn(person);
        mockMvc.perform(post("/persons")
                .content(objectMapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                .andExpect(jsonPath("$.country.name").value(person.getCountry().getName()))
                .andExpect(jsonPath("$.country.capital").value(person.getCountry().getCapital()))
                .andExpect(jsonPath("$.country.population").value(person.getCountry().getPopulation()))
                .andDo(document("createPerson"));
    }

    @Test
    public void testUpdatePerson() throws Exception {
        Person person = getPerson();
        given(personService.updatePerson(person.getId(), person)).willReturn(person);
        mockMvc.perform(put("/persons/{id}", person.getId())
                .content(objectMapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                .andExpect(jsonPath("$.country.name").value(person.getCountry().getName()))
                .andExpect(jsonPath("$.country.capital").value(person.getCountry().getCapital()))
                .andExpect(jsonPath("$.country.population").value(person.getCountry().getPopulation()))
                .andDo(document("updatePerson"));
    }

    private List<Person> getPersons() {
        return Arrays.asList(getPerson());
    }

    private Person getPerson() {
        Country country = new Country("France", "Paris", 1223333677);
        return new Person(1L, "Mamadou Lamine", "Ba", country);
    }

}