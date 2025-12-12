package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BreedDTORequest {
    private Long speciesId;
    private String name;
}
