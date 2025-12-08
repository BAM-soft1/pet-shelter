package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.SpeciesNode;
import org.pet.backendpetshelter.Neo4j.Service.SpeciesNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/species")
@CrossOrigin
@Profile("neo4j")
public class SpeciesNeo4jController {

    private final SpeciesNeo4jService speciesService;

    public SpeciesNeo4jController(SpeciesNeo4jService speciesService) {
        this.speciesService = speciesService;
    }

    @GetMapping
    public List<SpeciesNode> getAllSpecies() {
        return speciesService.getAllSpecies();
    }

    @GetMapping("/{id}")
    public SpeciesNode getSpeciesById(@PathVariable String id) {
        return speciesService.getSpeciesById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<SpeciesNode> addSpecies(@RequestBody SpeciesNode species) {
        return ResponseEntity.status(201).body(speciesService.addSpecies(species));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SpeciesNode> updateSpecies(@PathVariable String id, @RequestBody SpeciesNode species) {
        return ResponseEntity.ok(speciesService.updateSpecies(id, species));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSpecies(@PathVariable String id) {
        speciesService.deleteSpecies(id);
        return ResponseEntity.noContent().build();
    }
}
