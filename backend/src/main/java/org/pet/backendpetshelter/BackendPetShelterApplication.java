package org.pet.backendpetshelter;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendPetShelterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendPetShelterApplication.class, args);
        
        // Check if Sentry is initialized
        System.out.println("=== SENTRY CHECK ===");
        System.out.println("Sentry enabled: " + Sentry.isEnabled());
        
        try {
            var hub = Sentry.getCurrentHub();
            var options = hub.getOptions();
            System.out.println("Sentry DSN configured: " + (options.getDsn() != null && !options.getDsn().isEmpty()));
            System.out.println("Sentry DSN value: " + options.getDsn());
        } catch (Exception e) {
            System.out.println("Error checking Sentry config: " + e.getMessage());
        }
        
        System.out.println("===================");
        
        // Test sending an error
        System.out.println("Sending test error to Sentry...");
        Sentry.captureMessage("Backend started - Test from Spring Boot!");
    }

}
