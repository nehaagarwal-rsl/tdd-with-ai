package com.example.subscription;

/**
 * Thrown when an unrecognized voucher code is supplied to
 * {@link SubscriptionPricingService#calculateMonthlyPrice(String, int, String)}.
 *
 * <p>This is an unchecked exception since an invalid voucher code represents
 * an invalid caller input rather than a recoverable checked condition.
 */
public class InvalidVoucherException extends RuntimeException {

    public InvalidVoucherException(String message) {
        super(message);
    }
}
