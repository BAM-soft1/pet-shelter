package org.pet.backendpetshelter.Controller;

import org.pet.backendpetshelter.DTO.AdoptionRequest;
import org.pet.backendpetshelter.DTO.AdoptionResponse;
import org.pet.backendpetshelter.Service.AdoptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adoption")
@CrossOrigin
@Profile({"mysql", "test"})
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping
    public Page<AdoptionResponse> getAllAdoptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "adoptionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return adoptionService.GetAllAdoptions(pageable);
    }

    @GetMapping("/{id}")
    public AdoptionResponse getAdoptionById(Long id) {
        return adoptionService.GetAdoptionById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createAdoption(@RequestBody AdoptionRequest adoptionRequest) {
        return ResponseEntity.status(201).body(adoptionService.addAdoption(adoptionRequest));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdoptionResponse> updateAdoption(@PathVariable Long id, @RequestBody AdoptionRequest adoptionRequest) {
        AdoptionResponse updatedAdoption = adoptionService.updateAdoption(id, adoptionRequest);
        return ResponseEntity.ok(updatedAdoption);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }
}
