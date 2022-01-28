package com.spring.training.model;

import lombok.Data;

@Data
public class Country {
    private Long id;
    private String name;
    private String capital;
    private int population;
}
