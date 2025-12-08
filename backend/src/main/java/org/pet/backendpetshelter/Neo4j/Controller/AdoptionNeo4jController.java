package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.AdoptionNode;
import org.pet.backendpetshelter.Neo4j.Service.AdoptionNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/adoptions")
@CrossOrigin
@Profile("neo4j")
public class AdoptionNeo4jController {

    private final AdoptionNeo4jService adoptionService;

    public AdoptionNeo4jController(AdoptionNeo4jService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping
    public List<AdoptionNode> getAllAdoptions() {
        return adoptionService.getAllAdoptions();
    }

    @GetMapping("/{id}")
    public AdoptionNode getAdoptionById(@PathVariable String id) {
        return adoptionService.getAdoptionById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<AdoptionNode> addAdoption(@RequestBody AdoptionNode adoption) {
        return ResponseEntity.status(201).body(adoptionService.addAdoption(adoption));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdoptionNode> updateAdoption(@PathVariable String id, @RequestBody AdoptionNode adoption) {
        return ResponseEntity.ok(adoptionService.updateAdoption(id, adoption));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable String id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }
}
