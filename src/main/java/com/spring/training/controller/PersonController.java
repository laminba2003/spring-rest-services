package com.spring.training.controller;

import com.spring.training.model.Person;
import com.spring.training.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("persons")
@AllArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> getPersons() {
        log.debug("returning the list of persons");
        return personService.getPersons();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getPerson(@PathVariable("id") Long id) {
        log.debug("returning the person with id = {}", id);
        return personService.getPerson(id);
    }

}