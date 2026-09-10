package com.example.subscription;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("SubscriptionPricingService")
class SubscriptionPricingServiceTest {

private SubscriptionPricingService service;

@BeforeEach
void setUp() {
    service = new SubscriptionPricingService();
}

@Nested
@DisplayName("Base tier pricing (no discount, no voucher)")
class BaseTierPricing {

    @Test
    @DisplayName("BASIC at 6 months with no voucher returns exact base price of $50.00")
    void shouldReturnBasePriceForBasicTierUnderTwelveMonths() {
        BigDecimal price = service.calculateMonthlyPrice("BASIC", 6, null);
        assertEquals(new BigDecimal("50.00"), price);
    }

    @Test
    @DisplayName("PRO at 6 months with no voucher returns exact base price of $150.00")
    void shouldReturnBasePriceForProTierUnderTwelveMonths() {
        BigDecimal price = service.calculateMonthlyPrice("PRO", 6, null);
        assertEquals(new BigDecimal("150.00"), price);
    }

    @Test
    @DisplayName("ENTERPRISE at 6 months with no voucher returns exact base price of $500.00")
    void shouldReturnBasePriceForEnterpriseTierUnderTwelveMonths() {
        BigDecimal price = service.calculateMonthlyPrice("ENTERPRISE", 6, null);
        assertEquals(new BigDecimal("500.00"), price);
    }

    @Test
    @DisplayName("PRO at exactly 12 months (boundary) receives no longevity discount")
    void shouldApplyNoDiscountAtTwelveMonthBoundaryForPro() {
        BigDecimal price = service.calculateMonthlyPrice("PRO", 12, null);
        assertEquals(new BigDecimal("150.00"), price);
    }

    @Test
    @DisplayName("ENTERPRISE at exactly 36 months (boundary) receives no longevity discount")
    void shouldApplyNoDiscountAtThirtySixMonthBoundaryForEnterprise() {
        BigDecimal price = service.calculateMonthlyPrice("ENTERPRISE", 36, null);
        assertEquals(new BigDecimal("500.00"), price);
    }
}

@Nested
@DisplayName("Longevity discounts")
class LongevityDiscounts {

    @Test
    @DisplayName("BASIC at 13 months (more than 12) applies the 10% longevity discount ($50.00 -> $45.00)")
    void shouldApplyTenPercentLongevityDiscountJustAboveTwelveMonths() {
        BigDecimal price = service.calculateMonthlyPrice("BASIC", 13, null);
        assertEquals(new BigDecimal("45.00"), price);
    }

    @Test
    @DisplayName("ENTERPRISE at 37 months (more than 36) applies the 25% longevity discount ($500.00 -> $375.00)")
    void shouldApplyTwentyFivePercentLongevityDiscountJustAboveThirtySixMonths() {
        BigDecimal price = service.calculateMonthlyPrice("ENTERPRISE", 37, null);
        assertEquals(new BigDecimal("375.00"), price);
    }
}

@Nested
@DisplayName("Voucher behavior")
class VoucherBehavior {

    @Test
    @DisplayName("SAVE20 deducts a flat $20.00 after percentage discounts ($50.00 -> $30.00)")
    void shouldDeductFlatTwentyDollarsWithSave20Voucher() {
        BigDecimal price = service.calculateMonthlyPrice("BASIC", 6, "SAVE20");
        assertEquals(new BigDecimal("30.00"), price);
    }

    @Test
    @DisplayName("HALFPRICE halves the rate after longevity discount ($150.00 -10%-> $135.00 -> $67.50)")
    void shouldHalveRateAfterLongevityDiscountWithHalfPriceVoucher() {
        BigDecimal price = service.calculateMonthlyPrice("PRO", 13, "HALFPRICE");
        assertEquals(new BigDecimal("67.50"), price);
    }

    @Test
    @DisplayName("SAVE20 combined with the 25% longevity discount ($500.00 -25%-> $375.00 -> $355.00)")
    void shouldCombineSave20WithLongevityDiscount() {
        BigDecimal price = service.calculateMonthlyPrice("ENTERPRISE", 37, "SAVE20");
        assertEquals(new BigDecimal("355.00"), price);
    }

    @Test
    @DisplayName("A null voucher code means no voucher is applied; price remains the plain base price")
    void shouldApplyNoVoucherWhenVoucherCodeIsNull() {
        BigDecimal price = service.calculateMonthlyPrice("BASIC", 6, null);
        assertEquals(new BigDecimal("50.00"), price);
    }

    @Test
    @DisplayName("An unrecognized voucher code throws InvalidVoucherException")
    void shouldThrowInvalidVoucherExceptionForUnknownVoucherCode() {
        assertThrows(InvalidVoucherException.class,
                () -> service.calculateMonthlyPrice("BASIC", 6, "NOTAREALCODE"));
    }
}

@Nested
@DisplayName("Input contract validation")
class InputContractValidation {

    @Test
    @DisplayName("A negative month count throws IllegalArgumentException")
    void shouldThrowIllegalArgumentExceptionForNegativeMonths() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculateMonthlyPrice("BASIC", -1, null));
    }

    @Test
    @DisplayName("An unrecognized tier code throws IllegalArgumentException")
    void shouldThrowIllegalArgumentExceptionForInvalidTierCode() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculateMonthlyPrice("GOLD", 6, null));
    }
}

@Nested
@DisplayName("Minimum achievable documented price")
class MinimumAchievableDocumentedPrice {

    @Test
    @DisplayName("BASIC at 37 months with SAVE20 yields the lowest price obtainable from the "
            + "documented rules ($50.00 -25%-> $37.50 -> $17.50)")
    void shouldComputeMinimumAchievablePriceFromDocumentedDiscountRules() {
        BigDecimal price = service.calculateMonthlyPrice("BASIC", 37, "SAVE20");
        assertEquals(new BigDecimal("17.50"), price);
    }
}

@Nested
@DisplayName("Exact two-decimal-scale discount compounding")
class ExactTwoDecimalScaleCompounding {

    @Test
    @DisplayName("Compounding a 25% longevity discount with HALFPRICE resolves to a correctly "
            + "scaled two-decimal amount ($500.00 -> $375.00 -> $187.50)")
    void shouldReturnCorrectlyScaledAmountAfterMultipleDiscounts() {
        BigDecimal price = service.calculateMonthlyPrice("ENTERPRISE", 37, "HALFPRICE");
        assertEquals(new BigDecimal("187.50"), price);
        assertEquals(2, price.scale());
    }
}

}