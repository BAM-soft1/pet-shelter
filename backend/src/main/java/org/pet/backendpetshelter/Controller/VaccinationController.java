package org.pet.backendpetshelter.Controller;

import org.pet.backendpetshelter.DTO.VaccinationRequest;
import org.pet.backendpetshelter.DTO.VaccinationResponse;
import org.pet.backendpetshelter.Service.VaccinationService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaccination")
@CrossOrigin
@Profile({"mysql", "test"})
public class VaccinationController {


private final VaccinationService vaccinationService;

    public VaccinationController(VaccinationService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }


    @GetMapping
    public Page<VaccinationResponse> getVaccinations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateAdministered") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String animalStatus,
            @RequestParam(required = false) String search
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // If any filters are provided, use filtered query (native query requires database column names)
        if (animalStatus != null || search != null) {
            // Map Java field names to database column names for native queries
            String dbSortBy = sortBy.equals("dateAdministered") ? "date_administered" : 
                             sortBy.equals("nextDueDate") ? "next_due_date" : sortBy;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, dbSortBy));
            return vaccinationService.GetAllVaccinationsWithFilters(animalStatus, search, pageable);
        }
        
        // For non-filtered query, use Java field names (JPA will handle mapping)
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return vaccinationService.GetAllVaccinations(pageable);
    }


    @GetMapping("/{id}")
    public VaccinationResponse getVaccinationById(@PathVariable Long id){
        return vaccinationService.GetVaccinationById(id);
    }


    @PostMapping("/add")
    public ResponseEntity<VaccinationResponse> addVaccination(@RequestBody VaccinationRequest vaccinationRequest){
        return ResponseEntity.status(201).body(vaccinationService.addVaccination(vaccinationRequest));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<VaccinationResponse> updateVaccination(@PathVariable Long id, @RequestBody VaccinationRequest vaccinationRequest){
        VaccinationResponse updatedVaccination = vaccinationService.updateVaccination(id, vaccinationRequest);
        return ResponseEntity.ok(updatedVaccination);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable Long id){
        vaccinationService.deleteVaccination(id);
        return ResponseEntity.noContent().build();
    }


}