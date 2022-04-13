package com.spring.training.mapping;

import com.spring.training.domain.Person;
import com.spring.training.entity.PersonEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {

    Person toPerson(PersonEntity PersonEntity);

    PersonEntity fromPerson(Person Person);

}
