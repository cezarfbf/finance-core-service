package com.cezarfbf.finance.core.domain.fixedexpense;

import java.util.UUID;

public class FixedExpenseNotFoundException extends RuntimeException {

    public FixedExpenseNotFoundException(UUID id) {
        super("Fixed expense not found: " + id);
    }
}
