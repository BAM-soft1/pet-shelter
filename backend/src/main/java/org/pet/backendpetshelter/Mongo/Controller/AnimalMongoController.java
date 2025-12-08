package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.AnimalDocument;
import org.pet.backendpetshelter.Mongo.Service.AnimalMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/animals")
@CrossOrigin
@Profile("mongo")
public class AnimalMongoController {

    private final AnimalMongoService animalService;

    public AnimalMongoController(AnimalMongoService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<AnimalDocument> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/{id}")
    public AnimalDocument getAnimalById(@PathVariable String id) {
        return animalService.getAnimalById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<AnimalDocument> addAnimal(@RequestBody AnimalDocument animal) {
        return ResponseEntity.status(201).body(animalService.addAnimal(animal));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnimalDocument> updateAnimal(@PathVariable String id, @RequestBody AnimalDocument animal) {
        return ResponseEntity.ok(animalService.updateAnimal(id, animal));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable String id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
}
