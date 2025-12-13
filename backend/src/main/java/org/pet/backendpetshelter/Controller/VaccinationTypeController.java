package org.pet.backendpetshelter.Controller;


import org.pet.backendpetshelter.DTO.VaccinationTypeResponse;
import org.pet.backendpetshelter.Service.VaccinationTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vaccination-type")
@CrossOrigin
@Profile("mysql")
public class VaccinationTypeController {


    private final VaccinationTypeService vaccinationTypeService;

    public VaccinationTypeController(VaccinationTypeService vaccinationTypeService) {
        this.vaccinationTypeService = vaccinationTypeService;
    }


    @GetMapping
    public Page<VaccinationTypeResponse> getVaccinationTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "vaccineName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) Boolean requiredForAdoption,
            @RequestParam(required = false) String search
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // If any filters are provided, use filtered query
        if (requiredForAdoption != null || search != null) {
            return vaccinationTypeService.GetAllVaccinationTypesWithFilters(requiredForAdoption, search, pageable);
        }
        
        return vaccinationTypeService.GetAllVaccinationTypes(pageable);
    }

    @GetMapping("/{id}")
    public VaccinationTypeResponse getVaccinationTypeById(@PathVariable Long id) {
        return vaccinationTypeService.GetVaccinationTypeById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccinationTypeResponse> addVaccinationType(@RequestBody VaccinationTypeResponse vaccinationTypeRequest) {
        return ResponseEntity.status(201).body(vaccinationTypeService.addVaccinationType(vaccinationTypeRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccinationTypeResponse> updateVaccinationType(@PathVariable Long id, @RequestBody VaccinationTypeResponse vaccinationTypeRequest) {
        VaccinationTypeResponse updatedVaccinationType = vaccinationTypeService.updateVaccinationType(id, vaccinationTypeRequest);
        return ResponseEntity.ok(updatedVaccinationType);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccinationType(@PathVariable Long id) {
        vaccinationTypeService.deleteVaccinationType(id);
        return ResponseEntity.noContent().build();
    }

}