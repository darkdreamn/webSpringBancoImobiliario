package com.bancoImobiliario.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class House {
    private int rent;
    private int price;
    private int idHouse;
}
