package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VaccinationRequest {
    private Long animalId;
    private Long userId;
    private String dateAdministered;
    private Long vaccinationTypeId;
    private String nextDueDate;
}