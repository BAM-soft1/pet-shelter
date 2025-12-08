package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;
import org.pet.backendpetshelter.Entity.AdoptionApplication;

import java.util.Date;


@Getter
@Setter
public class AdoptionRequest {
    private AdoptionApplication adoptionApplication;
    private Date adoptionDate;
    private Boolean isActive;
}
