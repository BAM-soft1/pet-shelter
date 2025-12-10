package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;
import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Entity.User;
import org.pet.backendpetshelter.Status;

import java.util.Date;

@Getter
@Setter
public class AdminAdoptionApplicationResponse {
    private Long id;

    private User user;
    private Animal animal;

    private String description;
    private Date applicationDate;
    private Status status;

    private User reviewedByUserName;

    private Boolean isActive;

    public AdminAdoptionApplicationResponse(AdoptionApplication application) {
        this.id = application.getId();

        this.user = application.getUser();

        this.animal = application.getAnimal();
        this.description = application.getDescription();
        this.applicationDate = application.getApplicationDate();
        this.status = application.getStatus();

        this.reviewedByUserName = application.getReviewedByUser();

        this.isActive = application.getIsActive();

    }
}
