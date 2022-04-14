package com.spring.training.controller;

import com.spring.training.annotation.IsAdmin;
import com.spring.training.domain.Person;
import com.spring.training.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("persons")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public Page<Person> getPersons(Pageable pageable) {
        return personService.getPersons(pageable);
    }

    @GetMapping("{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return personService.getPerson(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @IsAdmin
    public Person createPerson(@Valid @RequestBody Person person) {
        return personService.createPerson(person);
    }

    @PutMapping("{id}")
    @IsAdmin
    public Person updatePerson(@PathVariable("id") Long id, @Valid @RequestBody Person person) {
        return personService.updatePerson(id, person);
    }

    @DeleteMapping("{id}")
    public void deletePerson(@PathVariable("id") Long id) {
        personService.deletePerson(id);
    }

}