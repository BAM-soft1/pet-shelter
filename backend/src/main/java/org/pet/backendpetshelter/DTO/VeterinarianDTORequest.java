package org.pet.backendpetshelter.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeterinarianDTORequest {
    private Long userId;
    private String licenseNumber;
    private String clinicName;
    private Boolean isActive;
}