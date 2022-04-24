package com.spring.training.service;

import com.spring.training.domain.Person;
import com.spring.training.exception.EntityNotFoundException;
import com.spring.training.exception.RequestException;
import com.spring.training.mapping.PersonMapper;
import com.spring.training.repository.CountryRepository;
import com.spring.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Locale;

@Service
@CacheConfig(cacheNames = "persons")
@AllArgsConstructor
public class PersonService {

    PersonRepository personRepository;
    CountryRepository countryRepository;
    PersonMapper personMapper;
    MessageSource messageSource;

    public Page<Person> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(personMapper::toPerson);
    }

    @Cacheable(key = "#id")
    public Person getPerson(Long id) {
        return personMapper.toPerson(personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("person.notfound", new Object[]{id},
                        Locale.getDefault()))));
    }

    @Transactional
    public Person createPerson(Person person) {
        countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("country.notfound",
                        new Object[]{person.getCountry().getName()},
                        Locale.getDefault())));
        person.setId(null);
        return personMapper.toPerson(personRepository.save(personMapper.fromPerson(person)));
    }

    @CachePut(key = "#id")
    @Transactional
    public Person updatePerson(Long id, Person person) {
        return personRepository.findById(id)
                .map(entity -> {
                    countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                            new EntityNotFoundException(messageSource.getMessage("country.notfound",
                                    new Object[]{person.getCountry().getName()},
                                    Locale.getDefault())));
                    person.setId(id);
                    return personMapper.toPerson(personRepository.save(personMapper.fromPerson(person)));
                }).orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("person.notfound",
                        new Object[]{id},
                        Locale.getDefault())));
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void deletePerson(Long id) {
        try {
            personRepository.deleteById(id);
        } catch (Exception e) {
            throw new RequestException(messageSource.getMessage("person.errordeletion", new Object[]{id},
                    Locale.getDefault()),
                    HttpStatus.CONFLICT);
        }
    }

}