package com.spring.training.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.training.BaseClass;
import com.spring.training.domain.Person;
import com.spring.training.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets/persons", uriPort = 9090)
public class PersonControllerTests extends BaseClass {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetPersons() throws Exception {
        List<Person> persons = Collections.singletonList(getPerson());
        Pageable pageable = PageRequest.of(1, 5);
        Page<Person> page = new PageImpl<>(persons, pageable, persons.size());
        given(personService.getPersons(pageable)).willReturn(page);
        mockMvc.perform(get("/persons")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[0].id").value(persons.get(0).getId()))
                .andExpect(jsonPath("$.content.[0].firstName").value(persons.get(0).getFirstName()))
                .andExpect(jsonPath("$.content.[0].lastName").value(persons.get(0).getLastName()))
                .andExpect(jsonPath("$.content.[0].country.name").value(persons.get(0).getCountry().getName()))
                .andExpect(jsonPath("$.content.[0].country.capital").value(persons.get(0).getCountry().getCapital()))
                .andExpect(jsonPath("$.content.[0].country.population").value(persons.get(0).getCountry().getPopulation()))
                .andDo(document("getPersons"))
                .andDo(document("getPersons", SpringCloudContractRestDocs.dslContract()));
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
                .andDo(document("getPerson"))
                .andDo(document("getPerson", SpringCloudContractRestDocs.dslContract()));
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
                .andDo(document("createPerson"))
                .andDo(document("createPerson", SpringCloudContractRestDocs.dslContract()));
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
                .andDo(document("updatePerson"))
                .andDo(document("updatePerson", SpringCloudContractRestDocs.dslContract()));
    }

    @Test
    public void testDeletePerson() throws Exception {
        Person person = getPerson();
        doNothing().when(personService).deletePerson(person.getId());
        mockMvc.perform(delete("/persons/{id}", person.getId()))
                .andExpect(status().isOk())
                .andDo(document("deletePerson"))
                .andDo(document("deletePerson", SpringCloudContractRestDocs.dslContract()));
    }

}