package org.pet.backendpetshelter.DTO;


import lombok.Getter;
import lombok.Setter;
import org.pet.backendpetshelter.Entity.Animal;
import org.pet.backendpetshelter.Entity.VaccinationType;
import org.pet.backendpetshelter.Entity.Veterinarian;

import java.util.Date;


@Getter
@Setter
public class VaccinationRequest {
    private Long animalId;
    private Long userId;
    private Date dateAdministered;
    private Long vaccinationTypeId;
    private Date nextDueDate;
}
