package org.pet.backendpetshelter.Controller;


import org.pet.backendpetshelter.DTO.AdminAdoptionApplicationResponse;
import org.pet.backendpetshelter.DTO.AdoptionApplicationRequest;
import org.pet.backendpetshelter.DTO.AdoptionApplicationResponse;
import org.pet.backendpetshelter.Service.AdoptionApplicationService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adoption-application")
@CrossOrigin
@Profile("mysql")
public class AdoptionApplicationController {

    private final AdoptionApplicationService adoptionApplicationService;

    public AdoptionApplicationController(AdoptionApplicationService adoptionApplicationService) {
        this.adoptionApplicationService = adoptionApplicationService;
    }

    @GetMapping("/all")
    public List<AdminAdoptionApplicationResponse> getAllAdoptionApplications() {
        return adoptionApplicationService.GetAllAdoptionApplications();
    }


    @GetMapping("/{id}")
    public AdoptionApplicationResponse getAdoptionApplicationById(Long id) {
        return adoptionApplicationService.GetAdoptionApplicationById(id);
    }

    @GetMapping("/user/{userId}")
    public List<AdoptionApplicationResponse> getAdoptionApplicationsForUser(@PathVariable Long userId) {
        return adoptionApplicationService.getAdoptionApplicationsForUser(userId);
    }

    @GetMapping("/has-applied/{userId}/{animalId}")
    public Boolean hasUserAppliedForAnimal(@PathVariable Long userId, @PathVariable Long animalId) {
        return adoptionApplicationService.hasUserAppliedForAnimal(userId, animalId);
    }

    @PostMapping("/add")
    public ResponseEntity<AdoptionApplicationResponse> addAdoptionApplication(@RequestBody AdoptionApplicationRequest request) {
        return ResponseEntity.status(201).body(adoptionApplicationService.addAdoptionApplication(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdoptionApplicationResponse> updateAdoptionApplication(@PathVariable Long id , @RequestBody AdoptionApplicationRequest request) {
        AdoptionApplicationResponse updatedApplication = adoptionApplicationService.updateAdoptionApplication(id, request);
        return ResponseEntity.ok(updatedApplication);

    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<AdoptionApplicationResponse> rejectAdoptionApplication(@PathVariable Long id, @RequestParam Long reviewedByUserId) {
        AdoptionApplicationResponse rejectedApplication = adoptionApplicationService.rejectAdoptionApplication(id, reviewedByUserId);
        return ResponseEntity.ok(rejectedApplication);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdoptionApplication(@PathVariable Long id){
        adoptionApplicationService.deleteAdoptionApplication(id);
        return ResponseEntity.noContent().build();
    }


}
