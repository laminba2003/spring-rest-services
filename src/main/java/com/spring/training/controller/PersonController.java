package com.spring.training.controller;

import com.spring.training.model.Person;
import com.spring.training.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("persons")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping(produces = "application/json")
    public List<Person> getPersons() {
        return personService.getPersons();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public Person getPerson(@PathVariable("id") Long id) {
        return personService.getPerson(id);
    }

}