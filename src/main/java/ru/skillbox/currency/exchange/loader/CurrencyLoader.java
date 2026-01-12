package ru.skillbox.currency.exchange.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.jaxb.CurrencyJaxb;
import ru.skillbox.currency.exchange.jaxb.CurrencyJaxbResponse;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * @author Дмитрий Ельцов
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyLoader {

    @Value("${cbr.xml.url}")
    private String xmlUrl;

    private final CurrencyRepository currencyRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void loadCurrencies() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String xml = restTemplate.getForObject(xmlUrl, String.class);

            JAXBContext context = JAXBContext.newInstance(CurrencyJaxbResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            assert xml != null;
            CurrencyJaxbResponse response = (CurrencyJaxbResponse) unmarshaller.unmarshal(new StringReader(xml));

            response.getCurrencies().stream()
                    .filter(c -> c.getCharCode() != null)
                    .forEach(this::saveOrUpdate);

            log.info("Currencies updated successfully");
            log.info("Loaded {} currencies", response.getCurrencies().size());
        } catch (Exception ex) {
            log.error("Failed to update currencies", ex);
        }
    }

    private void saveOrUpdate(CurrencyJaxb cbr) {
        Double value = Double.valueOf(cbr.getValue().replace(",", "."));
        currencyRepository.findByIsoCharCode(cbr.getCharCode())
                .ifPresentOrElse(
                        currency -> updateCurrency(cbr, currency, value),
                        () -> createCurrency(cbr, value)
                );
    }

    private void createCurrency(CurrencyJaxb cbr, Double value) {
        Currency currency =  Currency.builder()
                .name(cbr.getName())
                .nominal(cbr.getNominal())
                .isoCharCode(cbr.getCharCode())
                .isoNumCode(Long.valueOf(cbr.getNumCode()))
                .value(value)
                .build();

        currencyRepository.save(currency);
    }

    private void updateCurrency(CurrencyJaxb cbr, Currency currency, Double value) {
        currency.setName(cbr.getName());
        currency.setNominal(cbr.getNominal());
        currency.setValue(value);
    }

}
