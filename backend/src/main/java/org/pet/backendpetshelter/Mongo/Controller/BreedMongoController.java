package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.BreedDocument;
import org.pet.backendpetshelter.Mongo.Service.BreedMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/breeds")
@CrossOrigin
@Profile("mongo")
public class BreedMongoController {

    private final BreedMongoService breedService;

    public BreedMongoController(BreedMongoService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public List<BreedDocument> getAllBreeds() {
        return breedService.getAllBreeds();
    }

    @GetMapping("/{id}")
    public BreedDocument getBreedById(@PathVariable String id) {
        return breedService.getBreedById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<BreedDocument> addBreed(@RequestBody BreedDocument breed) {
        return ResponseEntity.status(201).body(breedService.addBreed(breed));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BreedDocument> updateBreed(@PathVariable String id, @RequestBody BreedDocument breed) {
        return ResponseEntity.ok(breedService.updateBreed(id, breed));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable String id) {
        breedService.deleteBreed(id);
        return ResponseEntity.noContent().build();
    }
}
