Act as a Senior Java Engineer implementing production code under strict Test-Driven Development.



The attached/current project contains an audited JUnit 5 test suite named SubscriptionPricingServiceTest.java.



Your task is to implement the minimum production code necessary to make the existing audited tests pass.



Important TDD rules:

- Treat the existing tests as the machine specification.

- Do NOT modify the tests.

- Do NOT weaken, remove, skip, or reinterpret any test assertion.

- Do NOT add @Disabled tests.

- Do NOT mock the class under test.

- Do NOT invent requirements that are not represented by the tests or business rules.

- Implement only the production classes required by the tests.

- Use Java 17-compatible code.

- Use BigDecimal for all currency calculations.

- Do not use double or float for monetary calculations.

- Return monetary results with exactly two decimal places.

- Use HALF_UP rounding to two decimal places.

- The final monthly price must never be below $0.00.



Required production classes:



1. SubscriptionPricingService

Package:

com.example.subscription



Required public API:

SubscriptionPricingService()



BigDecimal calculateMonthlyPrice(

    String tierCode,

    int months,

    String voucherCode

)



Business rules:

- BASIC = $50.00/month

- PRO = $150.00/month

- ENTERPRISE = $500.00/month

- More than 12 months = 10% longevity discount.

- More than 36 months = 25% longevity discount.

- Exactly 12 months receives no longevity discount.

- Exactly 36 months receives no longevity discount.

- SAVE20 deducts $20.00 after the longevity percentage discount.

- HALFPRICE reduces the calculated rate by 50% after the longevity discount.

- Null voucherCode means no voucher.

- Unknown voucher codes throw InvalidVoucherException.

- Negative months throw IllegalArgumentException.

- Unknown tier codes throw IllegalArgumentException.

- Apply the $0.00 floor to the final result.

- Monetary values must be rounded to two decimal places using HALF_UP.



2. InvalidVoucherException

Package:

com.example.subscription



Requirements:

- Must be a custom unchecked exception.

- It must be usable by SubscriptionPricingService when an unknown voucher code is supplied.



Implementation constraints:

- Keep the implementation minimal and production-oriented.

- Do not add unnecessary classes, frameworks, dependencies, or APIs.

- Do not change pom.xml unless absolutely required for compilation.

- Do not create an expiry-date API because the current specification does not provide one.

- Do not invent additional voucher types.

- Do not invent additional subscription tiers.

- Do not add unsupported business behavior merely to satisfy hypothetical cases.



Output:

Create the required production source files directly in the project.

Do not modify SubscriptionPricingServiceTest.java.


