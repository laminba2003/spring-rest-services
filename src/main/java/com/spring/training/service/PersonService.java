package com.spring.training.service;

import com.spring.training.domain.Person;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.mapping.PersonMapper;
import com.spring.training.repository.CountryRepository;
import com.spring.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "persons")
@AllArgsConstructor
public class PersonService {

    PersonRepository personRepository;
    CountryRepository countryRepository;
    PersonMapper personMapper;

    public Page<Person> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(personMapper::toPerson);
    }

    @Cacheable(key = "#id")
    public Person getPerson(Long id) {
        return personMapper.toPerson(personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("person not found with id = %d", id))));
    }

    public Person createPerson(Person person) {
        countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName())));
        person.setId(null);
        return personMapper.toPerson(personRepository.save(personMapper.fromPerson(person)));
    }

    @CachePut(key = "#id")
    public Person updatePerson(Long id, Person person) {
        return personRepository.findById(id)
                .map(entity -> {
                    countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                            new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName())));
                    person.setId(id);
                    return personMapper.toPerson(personRepository.save(personMapper.fromPerson(person)));
                }).orElseThrow(() -> new EntityNotFoundException(String.format("person not found with id = %d", id)));
    }

    @CacheEvict(key = "#id")
    public void deletePerson(Long id) {
        if(personRepository.existsById(id)) {
            personRepository.deleteById(id);
        }
    }

}