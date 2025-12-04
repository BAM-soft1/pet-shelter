package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.AnimalNode;
import org.pet.backendpetshelter.Neo4j.Service.AnimalNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/animals")
@CrossOrigin
@Profile("neo4j")
public class AnimalNeo4jController {

    private final AnimalNeo4jService animalService;

    public AnimalNeo4jController(AnimalNeo4jService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<AnimalNode> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/{id}")
    public AnimalNode getAnimalById(@PathVariable String id) {
        return animalService.getAnimalById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<AnimalNode> addAnimal(@RequestBody AnimalNode animal) {
        return ResponseEntity.status(201).body(animalService.addAnimal(animal));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnimalNode> updateAnimal(@PathVariable String id, @RequestBody AnimalNode animal) {
        return ResponseEntity.ok(animalService.updateAnimal(id, animal));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable String id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
}
