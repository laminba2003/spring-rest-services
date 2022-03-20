package com.spring.training.entity;

import com.spring.training.model.Person;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name="persons")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @ManyToOne
    private CountryEntity country;

    public Person toPerson() {
        return new Person(id, firstName, lastName, country.toCountry());
    }

}
