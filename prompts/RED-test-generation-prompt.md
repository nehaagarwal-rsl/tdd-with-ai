Act as a Senior QA Engineer specializing in Java 17, JUnit 5, and Test-Driven Development.



Target:

Generate a JUnit 5 unit test suite for a new Java class named SubscriptionPricingService. The production implementation does not exist yet. The tests must define the expected behavior of the service based only on the business rules below.



Business Rules:

1\. Subscription tier base monthly rates:

&#x20;  - BASIC: $50.00

&#x20;  - PRO: $150.00

&#x20;  - ENTERPRISE: $500.00



2\. Longevity discounts:

&#x20;  - Accounts active for more than 12 months receive a 10% discount on the monthly rate.

&#x20;  - Accounts active for more than 36 months receive a 25% discount on the monthly rate.

&#x20;  - The exact 12-month and 36-month boundaries must be treated as boundary conditions and tested explicitly.



3\. Promotional vouchers:

&#x20;  - "SAVE20" deducts an additional $20.00 flat amount after the applicable percentage-based longevity discount.

&#x20;  - "HALFPRICE" reduces the calculated rate by 50%, applied after the longevity discount.

&#x20;  - Invalid or expired voucher codes must result in a custom InvalidVoucherException.

&#x20;  - Voucher behavior must be tested independently and in combination with longevity discounts.



4\. Rounding and floor:

&#x20;  - The final monthly total must never be below $0.00.

&#x20;  - Currency calculations must be rounded accurately to exactly two decimal places using HALF\_UP rounding.

&#x20;  - Include test cases that can expose rounding errors.

&#x20;  - Include a case where a voucher discount could cause the calculated amount to become negative and verify that the final result is $0.00.



Test Coverage Requirements:

Generate tests covering:

\- All three subscription tiers and their base rates.

\- Normal longevity discount behavior.

\- Exact 12-month and 36-month boundaries.

\- More than 12 months and more than 36 months.

\- SAVE20 voucher behavior.

\- HALFPRICE voucher behavior.

\- Invalid voucher behavior and the custom InvalidVoucherException contract.

\- Voucher plus longevity-discount combinations.

\- Zero-floor behavior.

\- Currency rounding behavior.

\- Relevant null, empty, or invalid input cases where appropriate.

\- Use meaningful assertions that verify exact monetary results rather than weak assertions such as assertNotNull.

\- Do not mock the SubscriptionPricingService itself or the methods being tested.



Output Constraint:

Output ONLY runnable Java 17 JUnit 5 test code for SubscriptionPricingServiceTest.java.

Do NOT generate SubscriptionPricingService.java or any production implementation.

Do NOT modify, weaken, or omit assertions simply because the production class does not yet exist.

Use clear test method names and meaningful assertions.

