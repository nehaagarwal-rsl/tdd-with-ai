# TEST Phase - Adversarial Edge-Case Justifications

## Purpose

The TEST phase expands the audited JUnit 5 suite with adversarial boundary, validation, and extreme-value cases. The goal is to identify defects that ordinary happy-path tests may not expose while staying strictly within the documented API and business rules.

## Added Edge Cases

| Edge Case | Test Coverage | Why It Matters |
|---|---|---|
| Zero months | `shouldAcceptZeroMonths` | Zero is the lower valid boundary because the contract rejects negative months but does not prohibit zero. This verifies that a valid zero-month subscription receives the appropriate base price without accidentally triggering a discount or validation failure. |
| `Integer.MAX_VALUE` months | `shouldHandleMaximumIntegerMonthCount` | This is the largest possible Java `int` value accepted by the API. It verifies that an extreme but valid duration does not overflow or behave incorrectly and still follows the more-than-36-month discount rule. |
| `Integer.MIN_VALUE` months | `shouldRejectNegativeMonthBoundaries` | This is the smallest possible Java `int` value. It verifies that negative-duration validation is performed safely even at the most extreme invalid input rather than allowing the value to reach pricing calculations. |
| Null tier code | `shouldThrowIllegalArgumentExceptionForNullTierCode` | A missing tier is an invalid public API input. The test verifies that the service reports the documented `IllegalArgumentException` instead of leaking a `NullPointerException` or producing an unintended price. |
| Empty and malformed tier codes | `shouldRejectMalformedTierCodes` | Empty, incorrectly cased, and whitespace-altered tier codes verify that the service accepts only the documented exact tier codes and does not silently normalize unsupported input. |
| Invalid voucher variants | `shouldRejectMalformedVoucherCodes` | Unknown, incorrectly cased, and whitespace-altered voucher codes verify that only the documented exact voucher values are accepted and that unsupported vouchers consistently raise `InvalidVoucherException`. |
| Values around longevity boundaries | `shouldHandleValuesAroundLongevityBoundaries` | Parameterized boundary-adjacent cases verify behavior immediately below and above the 12- and 36-month thresholds. This reduces the risk of off-by-one errors in discount selection. |

## Parameterized Testing

Parameterized tests were used where multiple inputs exercise the same contract:

- Negative month values share the same validation rule.
- Malformed tier codes share the same invalid-input contract.
- Malformed voucher codes share the same exception contract.
- Values around longevity boundaries share the same threshold-selection logic.

This provides broader input coverage without duplicating test implementation.

## Coverage Limitation

The documented fixed tiers and vouchers cannot produce a negative calculated amount. The lowest documented result is BASIC at more than 36 months with SAVE20:

$50.00 -> $37.50 after the 25% longevity discount -> $17.50 after SAVE20.

Therefore, the final `$0.00` floor cannot be reached through any documented valid input. No unsupported tier, voucher, or artificial API was introduced merely to force coverage of that branch. The limitation is documented rather than hiding it behind an invented requirement.

## Verification

The complete JUnit 5 suite was executed with:

`mvn clean test`

Result:

- Tests run: 34
- Failures: 0
- Errors: 0
- Skipped: 0
- Build: SUCCESS