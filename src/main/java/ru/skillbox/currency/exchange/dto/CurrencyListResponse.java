package ru.skillbox.currency.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Дмитрий Ельцов
 */
@Getter
@Setter
@AllArgsConstructor
public class CurrencyListResponse {
    private List<CurrencyShortDto> currencies;
}
