package com.indra.retail;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountCalculatorTest {

    private static final BigDecimal ORIGINAL_PRICE = new BigDecimal("100.00");

    @ParameterizedTest
    @MethodSource("discountPolicies")
    @DisplayName("Debe aplicar el descuento correspondiente al tipo")
    void shouldApplyDiscount(DiscountCalculator calculator, BigDecimal expectedPrice) {
        assertEquals(expectedPrice, calculator.apply(ORIGINAL_PRICE));
    }

    @Test
    @DisplayName("Debe mantener el precio cuando no hay tipo de descuento")
    void shouldKeepPriceWithoutDiscount() {
        DiscountCalculator calculator = new NoDiscountPolicy();
        assertEquals(ORIGINAL_PRICE, calculator.apply(ORIGINAL_PRICE));
    }

    private static Stream<Arguments> discountPolicies() {
        return Stream.of(
                Arguments.of(new StandardDiscountPolicy(), new BigDecimal("95.0000")),
                Arguments.of(new SeasonalDiscountPolicy(), new BigDecimal("80.0000")),
                Arguments.of(new LoyaltyDiscountPolicy(), new BigDecimal("85.0000"))
        );
    }
}