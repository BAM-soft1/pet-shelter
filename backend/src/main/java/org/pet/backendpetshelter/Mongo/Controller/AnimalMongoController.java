package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.AnimalDocument;
import org.pet.backendpetshelter.Mongo.Service.AnimalMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/animal")
@CrossOrigin
@Profile("mongo")
public class AnimalMongoController {

    private final AnimalMongoService animalMongoService;

    public AnimalMongoController(AnimalMongoService animalMongoService) {
        this.animalMongoService = animalMongoService;
    }

    @GetMapping
    public List<AnimalDocument> getAllAnimals() {
        return animalMongoService.getAllAnimals();
    }

    @GetMapping("/{id}")
    public AnimalDocument getAnimalById(@PathVariable String id) {
        return animalMongoService.getAnimalById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<AnimalDocument> createAnimal(@RequestBody AnimalDocument animal) {
        return ResponseEntity.status(201).body(animalMongoService.createAnimal(animal));
    }

    @PutMapping("/update/{id}")
    public AnimalDocument updateAnimal(@PathVariable String id, @RequestBody AnimalDocument animal) {
        return animalMongoService.updateAnimal(id, animal);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable String id) {
        animalMongoService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
}