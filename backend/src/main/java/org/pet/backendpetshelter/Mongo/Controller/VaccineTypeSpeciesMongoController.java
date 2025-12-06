package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.VaccineTypeSpeciesDocument;
import org.pet.backendpetshelter.Mongo.Service.VaccineTypeSpeciesMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/vaccine-type-species")
@CrossOrigin
@Profile("mongo")
public class VaccineTypeSpeciesMongoController {

    private final VaccineTypeSpeciesMongoService vaccineTypeSpeciesService;

    public VaccineTypeSpeciesMongoController(VaccineTypeSpeciesMongoService vaccineTypeSpeciesService) {
        this.vaccineTypeSpeciesService = vaccineTypeSpeciesService;
    }

    @GetMapping
    public List<VaccineTypeSpeciesDocument> getAllVaccineTypeSpecies() {
        return vaccineTypeSpeciesService.getAllVaccineTypeSpecies();
    }

    @GetMapping("/{id}")
    public VaccineTypeSpeciesDocument getVaccineTypeSpeciesById(@PathVariable String id) {
        return vaccineTypeSpeciesService.getVaccineTypeSpeciesById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccineTypeSpeciesDocument> addVaccineTypeSpecies(@RequestBody VaccineTypeSpeciesDocument vaccineTypeSpecies) {
        return ResponseEntity.status(201).body(vaccineTypeSpeciesService.addVaccineTypeSpecies(vaccineTypeSpecies));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccineTypeSpeciesDocument> updateVaccineTypeSpecies(@PathVariable String id, @RequestBody VaccineTypeSpeciesDocument vaccineTypeSpecies) {
        return ResponseEntity.ok(vaccineTypeSpeciesService.updateVaccineTypeSpecies(id, vaccineTypeSpecies));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccineTypeSpecies(@PathVariable String id) {
        vaccineTypeSpeciesService.deleteVaccineTypeSpecies(id);
        return ResponseEntity.noContent().build();
    }
}
