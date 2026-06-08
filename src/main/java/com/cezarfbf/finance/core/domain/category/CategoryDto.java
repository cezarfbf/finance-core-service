package com.cezarfbf.finance.core.domain.category;

import java.util.UUID;

public record CategoryDto(UUID id, String code, String name, String icon, String color) {

    public static CategoryDto from(Category c) {
        return new CategoryDto(c.getId(), c.getCode(), c.getName(), c.getIcon(), c.getColor());
    }
}
