package org.pet.backendpetshelter.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MedicalRecordDTORequest {
     private Long animalId;       
    private Long veterinarianId; 
    private Date date;
    private String diagnosis;
    private String treatment;
    private int cost;
}