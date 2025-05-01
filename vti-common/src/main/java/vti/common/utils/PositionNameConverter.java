package vti.common.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import vti.common.enums.PositionName;

@Converter(autoApply = true)
public class PositionNameConverter implements AttributeConverter<PositionName, String> { //NOSONAR

    @Override
    public String convertToDatabaseColumn(PositionName attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public vti.common.enums.PositionName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : PositionName.valueOf(dbData.toUpperCase());
    }
}

