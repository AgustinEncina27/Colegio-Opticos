package com.colegio.opticos.colegiocuotas.model;

import java.time.YearMonth;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, String> {

    @Override
    public String convertToDatabaseColumn(YearMonth ym) {
        return ym != null ? ym.toString() : null;
    }

    @Override
    public YearMonth convertToEntityAttribute(String value) {
        return value != null ? YearMonth.parse(value) : null;
    }
}