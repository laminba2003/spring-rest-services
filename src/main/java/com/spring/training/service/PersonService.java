package com.spring.training.service;

import com.spring.training.entity.CountryEntity;
import com.spring.training.entity.PersonEntity;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.model.Person;
import com.spring.training.repository.CountryRepository;
import com.spring.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;

    public List<Person> getPersons() {
        return personRepository.findAll().stream().map(entity -> entity.toPerson()).collect(Collectors.toList());
    }

    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("person not found with id = %s", id)))
                .toPerson();
    }

    public Person createPerson(Person person) {
        Long id = Optional.ofNullable(person.getCountry().getId()).orElseThrow(() -> new NumberFormatException());
        countryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with id = %d", id)));
        return personRepository.save(PersonEntity.fromPerson(person)).toPerson();
    }

    public Person updatePerson(Person person) {
        Long id = Optional.ofNullable(person.getCountry().getId()).orElseThrow(() -> new NumberFormatException());
        CountryEntity countryEntity = countryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with id = %d", id)));
        return personRepository.findById(person.getId())
                .map(entity -> {
                    PersonEntity modified = PersonEntity.fromPerson(person);
                    modified.setId(entity.getId());
                    modified.setCountry(countryEntity);
                    return personRepository.save(modified).toPerson();
                }).orElseThrow(() -> new EntityNotFoundException(String.format("person not found with id = %d", person.getId())));
    }

}