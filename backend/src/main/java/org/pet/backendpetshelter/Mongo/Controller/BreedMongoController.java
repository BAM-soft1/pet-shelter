package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.BreedDocument;
import org.pet.backendpetshelter.Mongo.Service.BreedMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/breed")
@CrossOrigin
@Profile("mongo")
public class BreedMongoController {

    private final BreedMongoService breedService;

    public BreedMongoController(BreedMongoService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public List<BreedDocument> getAll() {
        return breedService.getAll();
    }

    @GetMapping("/{id}")
    public BreedDocument getById(@PathVariable String id) {
        return breedService.getById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<BreedDocument> create(@RequestBody BreedDocument breed) {
        return ResponseEntity.status(201).body(breedService.create(breed));
    }

    @PutMapping("/update/{id}")
    public BreedDocument update(@PathVariable String id, @RequestBody BreedDocument breed) {
        return breedService.update(id, breed);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        breedService.delete(id);
        return ResponseEntity.noContent().build();
    }
}