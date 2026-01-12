package ru.skillbox.currency.exchange.jaxb;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Дмитрий Ельцов
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyJaxb {

    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "NumCode")
    private String numCode;

    @XmlElement(name = "Nominal")
    private Long nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
    private String value;
}
