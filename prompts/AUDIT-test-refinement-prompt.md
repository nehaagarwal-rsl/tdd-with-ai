Act as a Senior QA Engineer reviewing and strengthening an existing JUnit 5 test suite for a Java 17 Test-Driven Development assignment.

Target:
Refine the existing SubscriptionPricingServiceTest.java test suite based on the audit findings below. The production class SubscriptionPricingService does not exist yet and must NOT be created or assumed to exist.

Audit Findings:
1. The zero-floor test named shouldApplyZeroFloorWhenSave20MakesPriceNegative is incorrect because BASIC at $50.00 minus SAVE20 at $20.00 results in $30.00, not a negative value. The test does not actually exercise the zero-floor rule.
2. The HALF_UP rounding test currently calculates an exact $225.00 amount and therefore does not exercise a fractional-cent rounding scenario.
3. The test named shouldRejectNullVoucherWhenVoucherIsProvided contradicts its own behavior because it expects a normal $50.00 result when the voucher is null.
4. Expired-voucher behavior cannot be meaningfully tested with the currently inferred API because the method accepts only a voucher code and no expiry information. Do not invent an expiry-date API.
5. Preserve meaningful exact monetary assertions and exception assertions.

Business Rules:
- BASIC: $50.00/month
- PRO: $150.00/month
- ENTERPRISE: $500.00/month
- More than 12 months: 10% longevity discount
- More than 36 months: 25% longevity discount
- Exactly 12 and exactly 36 months are boundary cases and receive no longevity discount.
- SAVE20 deducts an additional $20.00 after percentage discounts.
- HALFPRICE reduces the calculated rate by 50% after longevity discounts.
- Invalid voucher codes result in InvalidVoucherException.
- Final monthly price must never be below $0.00.
- Currency calculations must use HALF_UP rounding to two decimal places.

Refinement Requirements:
- Keep the test suite focused on business behavior.
- Use exact BigDecimal monetary assertions.
- Correct the misleading null-voucher test name if null means no voucher.
- Replace the ineffective zero-floor test with a scenario that can genuinely verify the $0.00 floor, using only behavior supported by the stated business rules and existing API design. If the current API makes the zero-floor rule untestable, clearly document that limitation rather than inventing unsupported inputs or behavior.
- Replace the ineffective HALF_UP test with a genuine rounding scenario only if the existing business rules and API can produce one. Do not invent arbitrary pricing rules merely to force rounding.
- Preserve the invalid voucher and invalid tier/negative-month tests where they represent reasonable input contracts.
- Do not weaken any existing meaningful assertions.
- Do not mock the SubscriptionPricingService.
- Do not modify the production implementation because it does not exist yet.
- Do not create SubscriptionPricingService.java or InvalidVoucherException.java.

Output Constraint:
Output ONLY the complete revised Java 17 JUnit 5 source code for SubscriptionPricingServiceTest.java.
