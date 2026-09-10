# AI Test Audit

## Audit Summary

The initial AI-generated test suite was reviewed against the assignment business rules and TDD test-quality requirements. The suite contains useful coverage, but several tests have weak or misleading logic that could allow incorrect production behavior to pass.

## AI Test Audit Matrix

| # | Test / Area | Identified Flaw | Why It Is a Problem | Corrective Fix |
|---|---|---|---|---|
| 1 | `shouldApplyZeroFloorWhenSave20MakesPriceNegative` | The test does not produce a negative amount. BASIC is $50.00 and SAVE20 produces $30.00. | The test name claims to verify the $0.00 floor, but the assertion only verifies a normal positive price. An implementation with no zero-floor protection could still pass this test. | Replace the scenario with a calculation that can actually become negative, or adjust the test/API design so a valid business-rule scenario reaches the floor and assert exactly `0.00`. |
| 2 | `shouldRoundCurrencyUsingHalfUp` | The calculation results in exactly $225.00 and does not create a fractional-cent rounding scenario. | The test verifies the scale is two decimals, but it does not prove HALF_UP rounding is implemented correctly. | Replace it with a calculation that produces a value requiring rounding and assert the exact expected HALF_UP result and two-decimal scale. |
| 3 | `shouldRejectNullVoucherWhenVoucherIsProvided` | The test name says null should be rejected, but the assertion expects the normal BASIC price of $50.00. | The test name and behavior contradict each other, making the intended contract unclear and weakening maintainability. | Rename the test to describe the actual contract, such as `shouldCalculateBasePriceWhenVoucherIsNull`, if null means no voucher. |
| 4 | Expired voucher coverage | The suite does not meaningfully test an expired voucher. | The assignment mentions expired/invalid vouchers, but the proposed method accepts only a voucher code and provides no expiry information. | Do not invent an expiry API. Document this as an unresolved contract limitation and test invalid voucher codes through the available API. |
| 5 | Invalid-input boundary coverage | Null or blank subscription tier behavior is not explicitly covered. | The assignment allows relevant invalid-input testing, but the exact contract for null/blank tier is not defined. | Keep the required invalid-tier test and avoid adding unsupported behavior until the production API contract is established. |

## Audit Conclusions

The suite has meaningful positive and negative assertions for tier pricing, longevity discounts, vouchers, and invalid inputs. However, the zero-floor and HALF_UP tests require correction because their current scenarios do not actually exercise the business rules they claim to verify.

The null-voucher test also requires a naming correction so the test communicates the actual expected behavior.

The expired-voucher requirement cannot be fully represented by the currently inferred method signature because no expiry date or voucher metadata is supplied. This limitation should be explicitly documented rather than solved by inventing an unsupported API.

## Audit Decision

The test suite must be strengthened before entering the GREEN phase. Production code must not be created merely to satisfy weak tests.

## Audited RED Verification

The audited test suite was verified with:

mvn clean test

Result: BUILD FAILURE

The failure is expected because the production classes have not yet been created. Maven successfully discovered and compiled the test source, then reported the missing production types:

- SubscriptionPricingService
- InvalidVoucherException

This confirms that the audited test suite is exercising the intended TDD RED state without requiring production implementation code to exist.

The audited test suite contains no @Disabled placeholder tests and no mocks of the class under test.
