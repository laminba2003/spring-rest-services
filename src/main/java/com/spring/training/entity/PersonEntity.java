package com.spring.training.entity;

import com.spring.training.model.Person;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="persons")
public class PersonEntity {

    @Id
    private Long id;
    private String firstName;
    private String lastName;

    public Person toPerson() {
        return Person.builder().id(id).firstName(firstName).lastName(lastName).build();
    }

}
