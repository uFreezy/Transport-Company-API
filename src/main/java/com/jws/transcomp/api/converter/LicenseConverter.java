package com.jws.transcomp.api.converter;


import com.jws.transcomp.api.models.base.LiscenceType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class LicenseConverter implements AttributeConverter<LiscenceType, String> {

    @Override
    public String convertToDatabaseColumn(LiscenceType lType) {
        if (lType == null) {
            return null;
        }
        return lType.name();
    }

    @Override
    public LiscenceType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(LiscenceType.values())
                .filter(c -> c.name().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}