package org.pet.backendpetshelter.unit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.pet.backendpetshelter.DTO.LoginRequest;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LoginRequest Validation Tests")
class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ==== TEST HELPER ====
    
    private LoginRequest createValidRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("SecurePassword123");
        return request;
    }

    @Nested
    @DisplayName("Valid Request Tests")
    class ValidRequestTests {

        // ==== EQUIVALENCE PARTITIONING - VALID PARTITION ====
        
        @Test
        @DisplayName("Should pass validation with all valid fields")
        void testValidRequestPassesValidation() {
            LoginRequest request = createValidRequest();

            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: BLANK/EMPTY EMAIL ====
        
        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Should fail validation when email is blank or empty")
        void testEmailBlankOrEmpty(String email) {
            LoginRequest request = createValidRequest();
            request.setEmail(email);

            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "Email should fail validation: '" + email + "'");
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                    "Should have email violation");
        }

        @Test
        @DisplayName("Should fail validation when email is null")
        void testEmailNull() {
            LoginRequest request = createValidRequest();
            request.setEmail(null);

            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "Null email should fail validation");
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                    "Should have email violation");
        }

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: INVALID EMAIL FORMAT ====
        
        @ParameterizedTest
        @MethodSource("provideInvalidEmails")
        @DisplayName("Should fail validation for invalid email formats")
        void testEmailInvalidFormat(String invalidEmail, String reason) {
            LoginRequest request = createValidRequest();
            request.setEmail(invalidEmail);

            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "Email should fail validation (" + reason + "): " + invalidEmail);
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                    "Should have email violation for: " + reason);
        }

        static Stream<org.junit.jupiter.params.provider.Arguments> provideInvalidEmails() {
            return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("userexample.com", "missing @"),
                org.junit.jupiter.params.provider.Arguments.of("user@", "missing domain"),
                org.junit.jupiter.params.provider.Arguments.of("@example.com", "missing local part")
            );
        }
    }

    @Nested
    @DisplayName("Password Validation Tests")
    class PasswordValidationTests {

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: BLANK PASSWORD ====
        
        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Should fail validation when password is blank or empty")
        void testPasswordBlankOrEmpty(String password) {
            LoginRequest request = createValidRequest();
            request.setPassword(password);

            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "Password should fail validation: '" + password + "'");
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")),
                    "Should have password violation");
        }

        @Test
        @DisplayName("Should fail validation when password is null")
        void testPasswordNull() {
            LoginRequest request = createValidRequest();
            request.setPassword(null);

            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "Null password should fail validation");
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")),
                    "Should have password violation");
        }

        // ==== EQUIVALENCE PARTITIONING - VALID PARTITION ====
        
        @ParameterizedTest
        @ValueSource(strings = {"a", "short", "NoSpecialChar", "VeryLongPasswordWithoutAnyConstraints123456789"})
        @DisplayName("Should pass validation with any non-blank password")
        void testPasswordNonBlank(String validPassword) {
            LoginRequest request = createValidRequest();
            request.setPassword(validPassword);

            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty(), "Password should be valid: " + validPassword);
        }
    }
}