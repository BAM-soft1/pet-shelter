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
import org.pet.backendpetshelter.DTO.RegisterUserRequest;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RegisterUserRequest Validation Tests")
class RegisterUserRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ==== TEST HELPER ====
    
    private RegisterUserRequest createValidRequest() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("user@example.com");
        request.setFirstName("Sergio");
        request.setLastName("Ramos");
        request.setPhone("+45 12345678");
        request.setPassword("SecureP@ss123");
        return request;
    }

    @Nested
    @DisplayName("Valid Request Tests")
    class ValidRequestTests {

        // ==== EQUIVALENCE PARTITIONING - VALID PARTITION ====
        
        @Test
        @DisplayName("Should pass validation with all valid fields")
        void testValidRequestPassesValidation() {
            RegisterUserRequest request = createValidRequest();

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation with phone as null (optional field)")
        void testValidRequestWithNullPhone() {
            RegisterUserRequest request = createValidRequest();
            request.setPhone(null);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: BLANK/NULL EMAIL ====
        
        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Should fail validation when email is blank or empty")
        void testEmailBlankOrEmpty(String email) {
            RegisterUserRequest request = createValidRequest();
            request.setEmail(email);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "Email should fail validation: '" + email + "'");
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("email")),
                    "Should have email violation");
        }

        @Test
        @DisplayName("Should fail validation when email is null")
        void testEmailNull() {
            RegisterUserRequest request = createValidRequest();
            request.setEmail(null);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

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
            RegisterUserRequest request = createValidRequest();
            request.setEmail(invalidEmail);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

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
    @DisplayName("FirstName Validation Tests")
    class FirstNameValidationTests {

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: BLANK FIRSTNAME ====
        
        @Test
        @DisplayName("Should fail validation when firstName is null")
        void testFirstNameNull() {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName(null);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }

        @Test
        @DisplayName("Should fail validation when firstName is empty")
        void testFirstNameEmpty() {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName("");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }

        // ==== BOUNDARY VALUE ANALYSIS: MIN LENGTH (2) ====
        
        @Test
        @DisplayName("Should fail validation when firstName is below min length (1 char)")
        void testFirstNameBelowMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName("J");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }

        @Test
        @DisplayName("Should pass validation when firstName is at min length (2 chars)")
        void testFirstNameAtMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName("Jo");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when firstName is above min length (3 chars)")
        void testFirstNameAboveMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName("Joe");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        // ==== BOUNDARY VALUE ANALYSIS: MAX LENGTH (80) ====
        
        @Test
        @DisplayName("Should pass validation when firstName is at max length (80 chars)")
        void testFirstNameAtMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName("J".repeat(80));

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should fail validation when firstName is above max length (81 chars)")
        void testFirstNameAboveMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName("J".repeat(81));

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        }
    }

    @Nested
    @DisplayName("LastName Validation Tests")
    class LastNameValidationTests {

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: BLANK LASTNAME ====
        
        @Test
        @DisplayName("Should fail validation when lastName is null")
        void testLastNameNull() {
            RegisterUserRequest request = createValidRequest();
            request.setLastName(null);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }

        @Test
        @DisplayName("Should fail validation when lastName is empty")
        void testLastNameEmpty() {
            RegisterUserRequest request = createValidRequest();
            request.setLastName("");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }

        // ==== BOUNDARY VALUE ANALYSIS: MIN LENGTH (2) ====
        
        @Test
        @DisplayName("Should fail validation when lastName is below min length (1 char)")
        void testLastNameBelowMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setLastName("D");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }

        @Test
        @DisplayName("Should pass validation when lastName is at min length (2 chars)")
        void testLastNameAtMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setLastName("Do");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when lastName is above min length (3 chars)")
        void testLastNameAboveMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setLastName("Doe");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        // ==== BOUNDARY VALUE ANALYSIS: MAX LENGTH (80) ====
        
        @Test
        @DisplayName("Should pass validation when lastName is at max length (80 chars)")
        void testLastNameAtMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setLastName("D".repeat(80));

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should fail validation when lastName is above max length (81 chars)")
        void testLastNameAboveMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setLastName("D".repeat(81));

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
        }
    }

    @Nested
    @DisplayName("Phone Validation Tests")
    class PhoneValidationTests {

        // ==== EQUIVALENCE PARTITIONING - VALID PARTITION ====
        
        @ParameterizedTest
        @ValueSource(strings = {"+45 12345678", "12345678", "+1-234-567-8901", "(123) 456-7890", "123-456-7890"})
        @DisplayName("Should pass validation with valid phone formats")
        void testValidPhoneFormats(String validPhone) {
            RegisterUserRequest request = createValidRequest();
            request.setPhone(validPhone);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty(), "Phone should be valid: " + validPhone);
        }

        // ==== BOUNDARY VALUE ANALYSIS: MIN LENGTH (7) ====
        
        @Test
        @DisplayName("Should fail validation when phone is below min length (6 chars)")
        void testPhoneBelowMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPhone("123456");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
        }

        @Test
        @DisplayName("Should pass validation when phone is at min length (7 chars)")
        void testPhoneAtMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPhone("1234567");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when phone is above min length (8 chars)")
        void testPhoneAboveMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPhone("12345678");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        // ==== BOUNDARY VALUE ANALYSIS: MAX LENGTH (32) ====
        
        @Test
        @DisplayName("Should pass validation when phone is below max length (31 chars)")
        void testPhoneBelowMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPhone("1".repeat(31));

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
        
        @Test
        @DisplayName("Should pass validation when phone is at max length (32 chars)")
        void testPhoneAtMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPhone("1".repeat(32));

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should fail validation when phone is above max length (33 chars)")
        void testPhoneAboveMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPhone("1".repeat(33));

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
        }

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: INVALID PATTERN ====
        
        @ParameterizedTest
        @ValueSource(strings = {"12345abc", "phone123", "123@456", "123#456"})
        @DisplayName("Should fail validation when phone contains invalid characters")
        void testPhoneInvalidCharacters(String invalidPhone) {
            RegisterUserRequest request = createValidRequest();
            request.setPhone(invalidPhone);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "Phone should be invalid: " + invalidPhone);
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("phone")),
                    "Should have phone violation");
        }
    }

    @Nested
    @DisplayName("Password Validation Tests")
    class PasswordValidationTests {

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: BLANK PASSWORD ====
        
        @Test
        @DisplayName("Should fail validation when password is null")
        void testPasswordNull() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword(null);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        }

        @Test
        @DisplayName("Should fail validation when password is empty")
        void testPasswordEmpty() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        }

        // ==== BOUNDARY VALUE ANALYSIS: MIN LENGTH (7) ====
        
        @Test
        @DisplayName("Should fail validation when password is below min length (6 chars)")
        void testPasswordBelowMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("Pass@1");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        }

        @Test
        @DisplayName("Should pass validation when password is at min length (7 chars) with special char")
        void testPasswordAtMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("Pass@12");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when password is above min length (8 chars)")
        void testPasswordAboveMinLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("Pass@123");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: MISSING SPECIAL CHAR ====
        
        @Test
        @DisplayName("Should fail validation when password lacks special character")
        void testPasswordMissingSpecialCharacter() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("Password123");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        }

        // ==== EQUIVALENCE PARTITIONING - VALID PARTITION: VARIOUS SPECIAL CHARS ====
        
        @ParameterizedTest
        @ValueSource(strings = {"Pass!123", "Pass@123", "Pass#123", "Pass$123", "Pass%123", "Pass^123", "Pass&123", "Pass*123"})
        @DisplayName("Should pass validation with various special characters")
        void testPasswordWithVariousSpecialCharacters(String validPassword) {
            RegisterUserRequest request = createValidRequest();
            request.setPassword(validPassword);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty(), "Password should be valid: " + validPassword);
        }

        // ==== BOUNDARY VALUE ANALYSIS: MAX LENGTH (60) ====
        
        @Test
        @DisplayName("Should pass validation when password is below max length (59 chars)")
        void testPasswordBelowMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("P@ssw0rd" + "X".repeat(51)); // 8 + 51 = 59 chars

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when password is at max length (60 chars)")
        void testPasswordAtMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("P@ssw0rd" + "X".repeat(52)); // 8 + 52 = 60 chars

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should fail validation when password is above max length (61 chars)")
        void testPasswordAboveMaxLength() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("P@ssw0rd" + "X".repeat(53)); // 8 + 53 = 61 chars

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        }

        // ==== EQUIVALENCE PARTITIONING - INVALID PARTITION: MISSING UPPERCASE ====
        
        @Test
        @DisplayName("Should fail validation when password lacks uppercase letter")
        void testPasswordMissingUppercase() {
            RegisterUserRequest request = createValidRequest();
            request.setPassword("password123!");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        }
    }

    @Nested
    @DisplayName("Email Boundary Tests")
    class EmailBoundaryTests {

        // ==== BOUNDARY VALUE ANALYSIS: MAX LENGTH (64) ====
        
        @Test
        @DisplayName("Should pass validation when email is at max length (64 chars)")
        void testEmailAtMaxLength() {
            RegisterUserRequest request = createValidRequest();
            // Create 64 char email - within @Email limits
            String localPart = "a".repeat(52); // 52 + "@example.com" (12) = 64
            request.setEmail(localPart + "@example.com");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when email is below reasonable length (64 chars)")
        void testEmailBelowMaxLength() {
            RegisterUserRequest request = createValidRequest();
            // Create 50 char email
            String localPart = "a".repeat(38); // 38 + "@example.com" (12) = 50
            request.setEmail(localPart + "@example.com");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should fail validation when email is above max length (64 chars)")
        void testEmailAboveMaxLength() {
            RegisterUserRequest request = createValidRequest();
            // Local part > 64 chars violates RFC 5321
            String localPart = "a".repeat(65);
            request.setEmail(localPart + "@example.com");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        }

        @Test
        @DisplayName("Should fail validation when email is missing local part")
        void testEmailMissingLocalPart() {
            RegisterUserRequest request = createValidRequest();
            request.setEmail("@example.com");

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        }
    }

    @Nested
    @DisplayName("FirstName Non-Alphabetic Tests")
    class FirstNameNonAlphabeticTests {

        @ParameterizedTest
        @ValueSource(strings = {"John-Doe", "John123", "John@", "John_Doe", "John.Doe"})
        @DisplayName("Should fail validation when firstName contains non-alphabetic characters")
        void testFirstNameWithNonAlphabetic(String invalidName) {
            RegisterUserRequest request = createValidRequest();
            request.setFirstName(invalidName);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "FirstName should be invalid: " + invalidName);
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")),
                    "Should have firstName violation");
        }
    }

    @Nested
    @DisplayName("LastName Non-Alphabetic Tests")
    class LastNameNonAlphabeticTests {

        @ParameterizedTest
        @ValueSource(strings = {"Ro-Bot", "Smith123", "Smith@", "Smith_Jones", "Smith.Jr"})
        @DisplayName("Should fail validation when lastName contains non-alphabetic characters")
        void testLastNameWithNonAlphabetic(String invalidName) {
            RegisterUserRequest request = createValidRequest();
            request.setLastName(invalidName);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertFalse(violations.isEmpty(), "LastName should be invalid: " + invalidName);
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")),
                    "Should have lastName violation");
        }
    }
}