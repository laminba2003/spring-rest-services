package com.spring.training.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Person {
    private Long id;
    private String firstName;
    private String lastName;
}
