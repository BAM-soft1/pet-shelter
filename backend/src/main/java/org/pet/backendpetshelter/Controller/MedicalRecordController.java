package org.pet.backendpetshelter.Controller;


import org.pet.backendpetshelter.DTO.MedicalRecordDTORequest;
import org.pet.backendpetshelter.DTO.MedicalRecordDTOResponse;
import org.pet.backendpetshelter.Service.MedicalRecordService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/medical-record")
@CrossOrigin
@Profile({"mysql", "test"})
public class MedicalRecordController {


    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }


    @GetMapping
    public Page<MedicalRecordDTOResponse> getMedicalRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String animalStatus,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) String search
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // If any filters are provided, use filtered query
        if (animalStatus != null || startDate != null || endDate != null || search != null) {
            return medicalRecordService.GetAllMedicalRecordsWithFilters(animalStatus, startDate, endDate, search, pageable);
        }
        
        return medicalRecordService.GetAllMedicalRecords(pageable);
    }

    @GetMapping("/{id}")
    public MedicalRecordDTOResponse getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @GetMapping("/animal/{animalId}")
    public List<MedicalRecordDTOResponse> getMedicalRecordsByAnimalId(@PathVariable Long animalId) {
        return medicalRecordService.getMedicalRecordsByAnimalId(animalId);
    }

    @PostMapping("/add")
    public ResponseEntity<MedicalRecordDTOResponse> addMedicalRecord(@RequestBody MedicalRecordDTORequest medicalRecordDTORequest) {
        return ResponseEntity.status(201).body(medicalRecordService.addMedicalRecord(medicalRecordDTORequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MedicalRecordDTOResponse> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecordDTORequest medicalRecordDTORequest) {
        MedicalRecordDTOResponse updatedMedicalRecord = medicalRecordService.updateMedicalRecord(id, medicalRecordDTORequest);
        return ResponseEntity.ok(updatedMedicalRecord);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id){
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}