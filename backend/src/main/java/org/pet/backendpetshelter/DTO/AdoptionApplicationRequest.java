package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdoptionApplicationRequest {
    private Long userId;
    private Long animalId;
    private String description;
}
