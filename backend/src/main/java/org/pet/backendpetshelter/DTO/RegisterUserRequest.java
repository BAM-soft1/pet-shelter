package org.pet.backendpetshelter.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.pet.backendpetshelter.Roles;

@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 2, max = 80)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank @Size(min = 2, max = 80)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastName;

    @Pattern(regexp = "^[0-9+\\-() ]{7,32}$", message = "Invalid phone format")
    private String phone;

    @NotBlank @Size(min = 7, max = 60, message = "Password must be between 7 and 60 characters")
    @Pattern(regexp = ".*[!@#$%^&*()_+=\\-{}:;\"'<>,.?/|\\[\\]\\\\].*",
            message = "Password must include at least one special character")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must include at least one uppercase letter")
    private String password;

    // Ignoreres ved register (vi sætter USER) – kun admin kan ændre senere
    private Roles role;
}
