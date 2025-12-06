package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.BreedNode;
import org.pet.backendpetshelter.Neo4j.Service.BreedNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/breeds")
@CrossOrigin
@Profile("neo4j")
public class BreedNeo4jController {

    private final BreedNeo4jService breedService;

    public BreedNeo4jController(BreedNeo4jService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public List<BreedNode> getAllBreeds() {
        return breedService.getAllBreeds();
    }

    @GetMapping("/{id}")
    public BreedNode getBreedById(@PathVariable String id) {
        return breedService.getBreedById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<BreedNode> addBreed(@RequestBody BreedNode breed) {
        return ResponseEntity.status(201).body(breedService.addBreed(breed));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BreedNode> updateBreed(@PathVariable String id, @RequestBody BreedNode breed) {
        return ResponseEntity.ok(breedService.updateBreed(id, breed));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable String id) {
        breedService.deleteBreed(id);
        return ResponseEntity.noContent().build();
    }
}
