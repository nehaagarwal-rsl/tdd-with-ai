import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SubscriptionPricingServiceTest {

    @Test
    void shouldReturnBasicTierBaseRate() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("BASIC", 0, null);

        assertEquals(new BigDecimal("50.00"), result);
    }

    @Test
    void shouldReturnProTierBaseRate() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("PRO", 0, null);

        assertEquals(new BigDecimal("150.00"), result);
    }

    @Test
    void shouldReturnEnterpriseTierBaseRate() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("ENTERPRISE", 0, null);

        assertEquals(new BigDecimal("500.00"), result);
    }

    @Test
    void shouldApplyTenPercentDiscountAfterMoreThanTwelveMonths() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("BASIC", 13, null);

        assertEquals(new BigDecimal("45.00"), result);
    }

    @Test
    void shouldNotApplyTenPercentDiscountAtExactlyTwelveMonths() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("BASIC", 12, null);

        assertEquals(new BigDecimal("50.00"), result);
    }

    @Test
    void shouldApplyTwentyFivePercentDiscountAfterMoreThanThirtySixMonths() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("PRO", 37, null);

        assertEquals(new BigDecimal("112.50"), result);
    }

    @Test
    void shouldNotApplyTwentyFivePercentDiscountAtExactlyThirtySixMonths() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("PRO", 36, null);

        assertEquals(new BigDecimal("150.00"), result);
    }

    @Test
    void shouldApplySave20VoucherAfterLongevityDiscount() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("PRO", 13, "SAVE20");

        assertEquals(new BigDecimal("115.00"), result);
    }

    @Test
    void shouldApplyHalfPriceVoucherAfterLongevityDiscount() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("PRO", 13, "HALFPRICE");

        assertEquals(new BigDecimal("67.50"), result);
    }

    @Test
    void shouldApplySave20VoucherWithoutLongevityDiscount() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("BASIC", 0, "SAVE20");

        assertEquals(new BigDecimal("30.00"), result);
    }

    @Test
    void shouldApplyHalfPriceVoucherWithoutLongevityDiscount() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("BASIC", 0, "HALFPRICE");

        assertEquals(new BigDecimal("25.00"), result);
    }

    @Test
    void shouldApplyZeroFloorWhenSave20MakesPriceNegative() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("BASIC", 0, "SAVE20");

        assertEquals(new BigDecimal("30.00"), result);
    }

    @Test
    void shouldRejectInvalidVoucher() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        assertThrows(
                InvalidVoucherException.class,
                () -> service.calculateMonthlyPrice("BASIC", 0, "INVALID")
        );
    }

    @Test
    void shouldRejectEmptyVoucher() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        assertThrows(
                InvalidVoucherException.class,
                () -> service.calculateMonthlyPrice("BASIC", 0, "")
        );
    }

    @Test
    void shouldRejectNullVoucherWhenVoucherIsProvided() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("BASIC", 0, null);

        assertEquals(new BigDecimal("50.00"), result);
    }

    @Test
    void shouldRejectInvalidTier() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.calculateMonthlyPrice("INVALID", 0, null)
        );
    }

    @Test
    void shouldRejectNegativeMonths() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.calculateMonthlyPrice("BASIC", -1, null)
        );
    }

    @Test
    void shouldRoundCurrencyUsingHalfUp() {
        SubscriptionPricingService service = new SubscriptionPricingService();

        BigDecimal result = service.calculateMonthlyPrice("ENTERPRISE", 13, "HALFPRICE");

        assertEquals(new BigDecimal("225.00"), result);
        assertEquals(2, result.scale());
    }
}