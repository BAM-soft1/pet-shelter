package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.AdoptionApplicationNode;
import org.pet.backendpetshelter.Neo4j.Service.AdoptionApplicationNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/adoption-applications")
@CrossOrigin
@Profile("neo4j")
public class AdoptionApplicationNeo4jController {

    private final AdoptionApplicationNeo4jService applicationService;

    public AdoptionApplicationNeo4jController(AdoptionApplicationNeo4jService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public List<AdoptionApplicationNode> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public AdoptionApplicationNode getApplicationById(@PathVariable String id) {
        return applicationService.getApplicationById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<AdoptionApplicationNode> addApplication(@RequestBody AdoptionApplicationNode application) {
        return ResponseEntity.status(201).body(applicationService.addApplication(application));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdoptionApplicationNode> updateApplication(@PathVariable String id, @RequestBody AdoptionApplicationNode application) {
        return ResponseEntity.ok(applicationService.updateApplication(id, application));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
