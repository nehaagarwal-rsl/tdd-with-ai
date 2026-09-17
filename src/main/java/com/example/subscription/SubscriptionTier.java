package com.example.subscription;

import java.math.BigDecimal;

/**
 * The set of subscription tiers supported by {@link SubscriptionPricingService},
 * each carrying its own fixed monthly base price.
 *
 * <p>This enum exists purely to give {@code tierCode} resolution and base-price
 * lookup a single, explicit home, replacing a parallel set of string constants
 * and a manual switch. The public {@code calculateMonthlyPrice(String, int, String)}
 * API is unaffected: callers still pass a plain tier code string, which is resolved
 * to a {@code SubscriptionTier} internally.
 */
enum SubscriptionTier {

    BASIC(new BigDecimal("50.00")),
    PRO(new BigDecimal("150.00")),
    ENTERPRISE(new BigDecimal("500.00"));

    private final BigDecimal basePrice;

    SubscriptionTier(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    BigDecimal basePrice() {
        return basePrice;
    }

    /**
     * Resolves a tier code string to its corresponding {@link SubscriptionTier}.
     *
     * @throws IllegalArgumentException if {@code tierCode} is {@code null} or does
     *                                  not match a supported tier
     */
    static SubscriptionTier fromCode(String tierCode) {
        if (tierCode == null) {
            throw new IllegalArgumentException("tierCode must not be null");
        }
        try {
            return SubscriptionTier.valueOf(tierCode);
        } catch (IllegalArgumentException notAKnownConstant) {
            throw new IllegalArgumentException("Unknown tier code: " + tierCode);
        }
    }
}
