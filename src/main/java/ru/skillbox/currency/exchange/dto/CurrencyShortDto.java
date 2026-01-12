package ru.skillbox.currency.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Дмитрий Ельцов
 */
@Getter
@Setter
@AllArgsConstructor
public class CurrencyShortDto {
    private String name;
    private Double value;
}
