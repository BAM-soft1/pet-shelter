package org.pet.backendpetshelter.Controller;


import org.pet.backendpetshelter.DTO.VaccineTypeSpeciesRequest;
import org.pet.backendpetshelter.DTO.VaccineTypeSpeciesResponse;
import org.pet.backendpetshelter.Service.VaccineTypeSpeciesService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccine-type-species")
@CrossOrigin
@Profile("mysql")
public class VaccineTypeSpeciesController {

    private final VaccineTypeSpeciesService vaccineTypeSpeciesService;

    public VaccineTypeSpeciesController(VaccineTypeSpeciesService vaccineTypeSpeciesService) {
        this.vaccineTypeSpeciesService = vaccineTypeSpeciesService;
    }


    @GetMapping
    public List<VaccineTypeSpeciesResponse> getAllVaccineTypeSpecies() {
        return vaccineTypeSpeciesService.GetAllVaccineTypeSpecies();
    }


    @GetMapping("/{id}")
    public VaccineTypeSpeciesResponse getVaccineTypeSpeciesById(@PathVariable Long id) {
        return vaccineTypeSpeciesService.GetVaccineTypeSpeciesById(id);
    }


    @PostMapping("/add")
    public ResponseEntity<VaccineTypeSpeciesResponse> addVaccineTypeSpecies(@RequestBody VaccineTypeSpeciesRequest request){
        return ResponseEntity.status(201).body(vaccineTypeSpeciesService.addVaccineTypeSpecies(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccineTypeSpeciesResponse> updateVaccineTypeSpecies(@PathVariable Long id, @RequestBody VaccineTypeSpeciesRequest request){
        VaccineTypeSpeciesResponse updatedVaccineTypeSpecies = vaccineTypeSpeciesService.updateVaccineTypeSpecies(id, request);
        return ResponseEntity.ok(updatedVaccineTypeSpecies);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccineTypeSpecies(@PathVariable Long id){
        vaccineTypeSpeciesService.deleteVaccineTypeSpecies(id);
        return ResponseEntity.noContent().build();
    }
}