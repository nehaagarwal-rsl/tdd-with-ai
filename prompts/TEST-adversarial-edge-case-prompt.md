# TEST Phase - Adversarial Edge-Case Expansion Prompt

## Role
Act as a senior Java test engineer reviewing an existing JUnit 5 test suite for a subscription pricing service.

## Context
The existing `SubscriptionPricingServiceTest` suite already covers:
- BASIC, PRO, and ENTERPRISE base prices
- 12-month and 36-month boundaries
- longevity discounts above 12 and 36 months
- SAVE20 and HALFPRICE vouchers
- null voucher behavior
- invalid voucher behavior
- negative months
- invalid tier
- documented minimum price
- two-decimal output scale

The production implementation has already passed the audited test suite and has been refactored using Java 17 features.

## Objective
Expand the test suite with adversarial, non-trivial edge cases that could expose boundary, validation, overflow, or contract-handling defects without inventing unsupported business rules or APIs.

## Required Coverage
Add at least four meaningful edge cases, including consideration of:

1. Zero months as the lower valid boundary.
2. Integer.MAX_VALUE months as an extreme valid duration.
3. Integer.MIN_VALUE as an extreme invalid negative duration.
4. Null tier code as invalid input.
5. Empty or incorrectly cased tier/voucher codes where the documented codes are exact uppercase values.

Use JUnit 5 parameterized tests where multiple inputs exercise the same contract.

## Test Quality Requirements
- Preserve all existing valid tests.
- Do not weaken or delete existing assertions.
- Do not add @Disabled tests.
- Do not use mocks.
- Do not invent an expiry API, clock, database, or additional tier.
- Do not test undocumented behavior as if it were a requirement.
- Use exact BigDecimal expected values with two decimal places.
- Verify exceptions with assertThrows.
- Keep tests deterministic and readable.
- Explain why each newly added edge case is valuable.

## Important Specification Constraint
The documented fixed tiers and vouchers cannot actually produce a negative final price:
the lowest documented result is BASIC at more than 36 months with SAVE20,
which is $17.50. Therefore, do not fabricate an input merely to force the zero-floor branch. Document this as a coverage limitation instead.

## Expected Deliverables
1. Updated `SubscriptionPricingServiceTest.java`.
2. A parameterized test where appropriate.
3. At least four non-trivial adversarial edge cases.
4. A written justification for each newly added edge case.
5. Full test suite passing with `mvn clean test`.