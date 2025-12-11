package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;
import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Status;

import java.util.Date;

@Getter
@Setter
public class AdoptionApplicationResponse {
    private Long id;

    // Applicant user – restricted
    private Long userId;
    private String userName;

    private Animal animal;

    private String description;
    private Date applicationDate;
    private Status status;

    // Reviewer – only name
    private String reviewedByUserName;

    private Boolean isActive;

    public AdoptionApplicationResponse(AdoptionApplication application) {
        this.id = application.getId();

        if (application.getUser() != null) {
            this.userId = application.getUser().getId();
            this.userName = application.getUser().getFirstName();
        }

        this.animal = application.getAnimal();
        this.description = application.getDescription();
        this.applicationDate = application.getApplicationDate();
        this.status = application.getStatus();

        if (application.getReviewedByUser() != null) {
            this.reviewedByUserName = application.getReviewedByUser().getFirstName();
        }

        this.isActive = application.getIsActive();
    }
}