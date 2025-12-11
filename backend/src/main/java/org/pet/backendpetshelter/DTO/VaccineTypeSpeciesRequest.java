package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VaccineTypeSpeciesRequest {
    private Long speciesId;
    private Long vaccinationTypeId;
}