package com.indra.retail;

import java.math.BigDecimal;

public interface DiscountCalculator {

    BigDecimal apply(BigDecimal price);
}

final class StandardDiscountPolicy implements DiscountCalculator {

    @Override
    public BigDecimal apply(BigDecimal price) {
        return price.multiply(new BigDecimal("0.95"));
    }
}

final class SeasonalDiscountPolicy implements DiscountCalculator {

    @Override
    public BigDecimal apply(BigDecimal price) {
        return price.multiply(new BigDecimal("0.80"));
    }
}

final class LoyaltyDiscountPolicy implements DiscountCalculator {

    @Override
    public BigDecimal apply(BigDecimal price) {
        return price.multiply(new BigDecimal("0.85"));
    }
}

final class NoDiscountPolicy implements DiscountCalculator {

    @Override
    public BigDecimal apply(BigDecimal price) {
        return price;
    }
}