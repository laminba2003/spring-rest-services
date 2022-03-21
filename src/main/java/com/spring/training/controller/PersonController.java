package com.spring.training.controller;

import com.spring.training.model.Person;
import com.spring.training.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("persons")
@AllArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<Person> getPersons() {
        log.debug("returning the list of persons");
        return personService.getPersons();
    }

    @GetMapping("{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        log.debug("returning the person with id = {}", id);
        return personService.getPerson(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Person createPerson(@Valid @RequestBody Person person) {
        log.debug("creating person with values = {}", person);
        return personService.createPerson(person);
    }

    @PutMapping("{id}")
    public Person updatePerson(@PathVariable("id") Long id, @Valid @RequestBody Person person) {
        log.debug("updating person with id = {} and values = {}", person);
        return personService.updatePerson(id, person);
    }

}