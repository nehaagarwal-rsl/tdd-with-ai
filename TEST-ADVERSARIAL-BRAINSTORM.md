# TEST Phase - AI Adversarial Edge-Case Brainstorm

## Purpose

The AI-assisted TEST phase was used to brainstorm adversarial edge cases that could expose boundary, validation, overflow, rounding, or contract-handling defects in the existing `SubscriptionPricingService` test suite.

The brainstorm was constrained to the documented API and business rules. Unsupported APIs, invented business rules, and artificial inputs intended only to force unreachable branches were excluded.

## AI Brainstorm List

### 1. Zero-month subscription
- Test `months = 0`.
- Verify that zero is accepted because the documented validation rule rejects negative months.
- Verify that no longevity discount is applied.
- Verify the exact expected `BigDecimal` result.

### 2. Integer.MAX_VALUE months
- Test `months = Integer.MAX_VALUE`.
- Verify that the extreme valid `int` value does not overflow or cause unexpected behavior.
- Verify that it follows the documented more-than-36-month longevity discount.

### 3. Integer.MIN_VALUE months
- Test `months = Integer.MIN_VALUE`.
- Verify that the extreme negative value is rejected with `IllegalArgumentException`.
- Confirm that validation occurs before pricing calculations.

### 4. Exact longevity boundaries
- Recheck exactly 12 months.
- Recheck exactly 36 months.
- Test values immediately below and above the boundaries, including 11, 13, 35, and 37 months.
- Look specifically for off-by-one errors in discount selection.

### 5. Invalid voucher codes
- Test an unknown voucher.
- Test lowercase versions of documented vouchers.
- Test whitespace-altered voucher codes.
- Verify that unsupported voucher codes consistently throw `InvalidVoucherException`.

### 6. Invalid and malformed tier codes
- Test a null tier.
- Test an empty tier.
- Test incorrectly cased tier codes.
- Test whitespace-altered tier codes.
- Verify that unsupported tier values throw `IllegalArgumentException`.

### 7. Rounding behavior
- Review whether the documented calculation can produce fractional-cent values.
- Verify that returned prices use two decimal places and the documented `HALF_UP` rounding mode.
- Do not invent inputs or APIs solely to manufacture a fractional-cent scenario if the fixed documented rates cannot produce one.

### 8. Zero-floor behavior
- Investigate whether any documented combination of tier, longevity discount, and voucher can produce a negative price.
- The documented minimum valid result is BASIC at more than 36 months with SAVE20:
  `$50.00 -> $37.50 -> $17.50`.
- Therefore, the `$0.00` floor cannot be reached through the documented valid inputs.
- Document this as a coverage limitation rather than inventing unsupported business rules.

### 9. Null voucher behavior
- Verify that a null voucher means no voucher is applied.
- Ensure that null handling does not accidentally trigger invalid-voucher behavior.

### 10. Combined discount and voucher behavior
- Verify that longevity discounting occurs before voucher application.
- Verify SAVE20 uses the post-longevity amount.
- Verify HALFPRICE uses the post-longevity amount.

## Selected TEST Cases

The following cases were selected for implementation because they provide meaningful coverage of validation, extreme values, malformed input, and boundary behavior without inventing unsupported requirements:

1. Zero months.
2. Integer.MAX_VALUE months.
3. Integer.MIN_VALUE months.
4. Null tier code.
5. Empty, lowercase, and whitespace-altered tier codes.
6. Invalid, lowercase, and whitespace-altered voucher codes.
7. Values immediately around the 12- and 36-month longevity boundaries.

## Test Selection Rationale

The selected cases target the areas most likely to contain defects that ordinary happy-path tests can miss: lower and upper integer boundaries, exact threshold transitions, strict input contracts, and exception handling.

The resulting tests use JUnit 5 parameterization where multiple inputs exercise the same contract. Existing valid tests and assertions were preserved.

## Coverage Limitation

The AI brainstorm considered the `$0.00` floor and rounding behavior. The documented fixed tiers and vouchers do not provide a valid input that reaches a negative final amount, and the fixed documented percentages do not create a meaningful fractional-cent calculation that would exercise a rounding transition.

These limitations were documented rather than addressed by inventing unsupported APIs, tiers, vouchers, clocks, or business rules.
