package com.driven.dm.global.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static double round1(Double v) {
        if (v == null) {
            return 0.0;
        }
        return BigDecimal.valueOf(v)
            .setScale(1, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
