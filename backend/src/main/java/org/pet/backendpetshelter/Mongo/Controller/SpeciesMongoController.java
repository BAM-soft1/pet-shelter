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
    public List<SpeciesDocument> getAll() {
        return speciesService.getAll();
    }

    @GetMapping("/{id}")
    public SpeciesDocument getById(@PathVariable String id) {
        return speciesService.getById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<SpeciesDocument> create(@RequestBody SpeciesDocument species) {
        return ResponseEntity.status(201).body(speciesService.create(species));
    }

    @PutMapping("/update/{id}")
    public SpeciesDocument update(@PathVariable String id, @RequestBody SpeciesDocument species) {
        return speciesService.update(id, species);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        speciesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}