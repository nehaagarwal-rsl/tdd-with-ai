# Reflection - Test Driven Development with AI

## Overview

This assignment demonstrated how AI can accelerate software development without replacing engineering judgment. The most important lesson was that AI is most useful when it operates inside a disciplined TDD workflow with clear contracts, explicit constraints, and human review at every stage.

## RED Phase

The RED phase established the specification through tests before production code existed. Asking AI to generate JUnit 5 tests quickly created broad initial coverage, but the tests could not be accepted blindly. This reinforced the principle that tests are executable requirements and must be reviewed for correctness, relevance, and completeness.

## AUDIT Phase

The audit exposed weaknesses in AI-generated tests, including ineffective coverage around rounding and the zero-floor branch, as well as the limitation that voucher expiry could not be tested because the provided API contained no expiry or clock mechanism. This was a key lesson: AI can confidently generate tests for behavior that the actual contract cannot support. Human review is therefore essential for identifying tautologies, invented requirements, and misleading assertions.

## GREEN Phase

After auditing, the tests became the contract for the minimal production implementation. The implementation was intentionally kept focused on the documented business rules rather than adding speculative functionality. Running the complete suite provided objective evidence that the implementation satisfied the accepted requirements.

## REFACTOR Phase

The refactoring phase showed that TDD does not end when tests pass. The implementation was modernized using Java 17 features, including an enum for tier data and a switch expression for voucher handling. The full test suite remained green, demonstrating that structural improvements can be made safely when behavior is protected by tests.

## TEST Phase

The adversarial testing phase expanded coverage around boundaries, malformed inputs, and extreme integer values. Parameterized tests made it possible to exercise multiple related cases without duplicating test logic. The exercise also demonstrated the importance of documenting coverage limitations rather than inventing unsupported inputs simply to increase branch coverage.

## AI Strengths and Limitations

AI was valuable for generating test ideas, implementation structure, refactoring suggestions, and additional edge cases quickly. However, AI output required continuous verification. It could produce assumptions that were not supported by the specification, so the engineer remained responsible for deciding what behavior was actually required.

## Key Takeaway

The strongest lesson was that AI should act as a pair programmer, not the final authority. The human engineer remains the architect and quality gate: defining the contract, challenging AI-generated assumptions, validating tests, reviewing production code, and deciding whether the final result is trustworthy.