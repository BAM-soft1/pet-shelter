package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VaccinationRequest {
    private Long animalId;
    private Long vaccinationTypeId;
    private String dateAdministered;
    private String nextDueDate;
}