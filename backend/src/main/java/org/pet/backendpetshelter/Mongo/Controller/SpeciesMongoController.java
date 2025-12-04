package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.SpeciesDocument;
import org.pet.backendpetshelter.Mongo.Service.SpeciesMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/species")
@CrossOrigin
@Profile("mongo")
public class SpeciesMongoController {

    private final SpeciesMongoService speciesService;

    public SpeciesMongoController(SpeciesMongoService speciesService) {
        this.speciesService = speciesService;
    }

    @GetMapping
    public List<SpeciesDocument> getAllSpecies() {
        return speciesService.getAllSpecies();
    }

    @GetMapping("/{id}")
    public SpeciesDocument getSpeciesById(@PathVariable String id) {
        return speciesService.getSpeciesById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<SpeciesDocument> addSpecies(@RequestBody SpeciesDocument species) {
        return ResponseEntity.status(201).body(speciesService.addSpecies(species));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SpeciesDocument> updateSpecies(@PathVariable String id, @RequestBody SpeciesDocument species) {
        return ResponseEntity.ok(speciesService.updateSpecies(id, species));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSpecies(@PathVariable String id) {
        speciesService.deleteSpecies(id);
        return ResponseEntity.noContent().build();
    }
}
