Act as a Senior Java 17 Engineer performing a controlled TDD refactoring.

The current project contains a production implementation of SubscriptionPricingService and an audited JUnit 5 test suite named SubscriptionPricingServiceTest.java.

The existing implementation is GREEN:
- All existing tests pass.
- Do not change the tests.
- Do not change their assertions.
- Do not add @Disabled tests.
- Do not weaken or remove any behavior.

Refactoring Goal:
Refactor the production implementation to use modern Java 17 language features, especially switch expressions where they improve clarity and reduce procedural conditional logic.

Requirements:
1. Preserve every existing business rule and observable behavior.
2. Preserve the public API:
   BigDecimal calculateMonthlyPrice(String tierCode, int months, String voucherCode)
3. Continue using BigDecimal for all monetary calculations.
4. Continue using HALF_UP rounding to exactly two decimal places.
5. Preserve the .00 floor.
6. Preserve:
   - BASIC = .00
   - PRO = .00
   - ENTERPRISE = .00
   - more than 12 and fewer than 36 months = 10% discount
   - exactly 12 months = no discount
   - exactly 36 months = no discount
   - more than 36 months = 25% discount
   - SAVE20 = subtract .00 after longevity discount
   - HALFPRICE = 50% after longevity discount
   - null voucher = no voucher
   - invalid voucher = InvalidVoucherException
   - negative months = IllegalArgumentException
   - invalid tier = IllegalArgumentException

Refactoring Constraints:
- Use Java 17-compatible switch expressions where appropriate.
- Prefer clear, maintainable production code over clever abstractions.
- Do not introduce unnecessary frameworks or dependencies.
- Do not change pom.xml unless absolutely necessary.
- Do not invent new business requirements.
- Do not create unsupported voucher types or expiry APIs.
- Do not modify SubscriptionPricingServiceTest.java.
- Keep the implementation production-oriented.
- If a small supporting production type such as an enum provides a genuine improvement, it may be introduced, but avoid unnecessary over-engineering.
- The refactor must result in a meaningful multi-file production diff if a supporting type is genuinely justified.
- Preserve package structure and public API compatibility.

Output:
Apply the refactoring directly to the current project.
Do not modify the test suite.
