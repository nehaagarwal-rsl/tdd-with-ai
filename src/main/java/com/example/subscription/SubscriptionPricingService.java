package com.example.subscription;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates monthly subscription prices based on tier, subscription length
 * (longevity discounts), and an optional voucher code.
 *
 * <p>Business rules:
 * <ul>
 *   <li>BASIC = $50.00/month, PRO = $150.00/month, ENTERPRISE = $500.00/month.</li>
 *   <li>More than 12 months (and 36 or fewer) applies a 10% longevity discount.</li>
 *   <li>More than 36 months applies a 25% longevity discount.</li>
 *   <li>Exactly 12 or exactly 36 months receives no longevity discount.</li>
 *   <li>SAVE20 deducts a flat $20.00 after the longevity percentage discount.</li>
 *   <li>HALFPRICE reduces the post-longevity-discount rate by 50%.</li>
 *   <li>A null voucher code means no voucher is applied.</li>
 *   <li>An unrecognized voucher code throws {@link InvalidVoucherException}.</li>
 *   <li>A negative month count or an unrecognized tier code throws
 *       {@link IllegalArgumentException}.</li>
 *   <li>The final price is floored at $0.00 and rounded HALF_UP to two decimal places.</li>
 * </ul>
 */
public class SubscriptionPricingService {

    private static final BigDecimal BASIC_BASE_PRICE = new BigDecimal("50.00");
    private static final BigDecimal PRO_BASE_PRICE = new BigDecimal("150.00");
    private static final BigDecimal ENTERPRISE_BASE_PRICE = new BigDecimal("500.00");

    private static final BigDecimal TEN_PERCENT_DISCOUNT_MULTIPLIER = new BigDecimal("0.90");
    private static final BigDecimal TWENTY_FIVE_PERCENT_DISCOUNT_MULTIPLIER = new BigDecimal("0.75");
    private static final BigDecimal HALF_MULTIPLIER = new BigDecimal("0.50");

    private static final BigDecimal SAVE20_DEDUCTION = new BigDecimal("20.00");

    private static final BigDecimal ZERO_FLOOR = new BigDecimal("0.00");

    private static final String TIER_BASIC = "BASIC";
    private static final String TIER_PRO = "PRO";
    private static final String TIER_ENTERPRISE = "ENTERPRISE";

    private static final String VOUCHER_SAVE20 = "SAVE20";
    private static final String VOUCHER_HALFPRICE = "HALFPRICE";

    public SubscriptionPricingService() {
    }

    public BigDecimal calculateMonthlyPrice(String tierCode, int months, String voucherCode) {
        if (months < 0) {
            throw new IllegalArgumentException("months must not be negative: " + months);
        }

        BigDecimal basePrice = resolveBasePrice(tierCode);
        BigDecimal afterLongevityDiscount = applyLongevityDiscount(basePrice, months);
        BigDecimal afterVoucher = applyVoucher(afterLongevityDiscount, voucherCode);

        BigDecimal floored = afterVoucher.max(ZERO_FLOOR);
        return floored.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal resolveBasePrice(String tierCode) {
        if (tierCode == null) {
            throw new IllegalArgumentException("tierCode must not be null");
        }
        switch (tierCode) {
            case TIER_BASIC:
                return BASIC_BASE_PRICE;
            case TIER_PRO:
                return PRO_BASE_PRICE;
            case TIER_ENTERPRISE:
                return ENTERPRISE_BASE_PRICE;
            default:
                throw new IllegalArgumentException("Unknown tier code: " + tierCode);
        }
    }

    private BigDecimal applyLongevityDiscount(BigDecimal basePrice, int months) {
        if (months > 36) {
            return basePrice.multiply(TWENTY_FIVE_PERCENT_DISCOUNT_MULTIPLIER);
        }
        if (months > 12 && months < 36) {
            return basePrice.multiply(TEN_PERCENT_DISCOUNT_MULTIPLIER);
        }
        return basePrice;
    }

    private BigDecimal applyVoucher(BigDecimal amount, String voucherCode) {
        if (voucherCode == null) {
            return amount;
        }
        switch (voucherCode) {
            case VOUCHER_SAVE20:
                return amount.subtract(SAVE20_DEDUCTION);
            case VOUCHER_HALFPRICE:
                return amount.multiply(HALF_MULTIPLIER);
            default:
                throw new InvalidVoucherException("Unknown voucher code: " + voucherCode);
        }
    }
}

