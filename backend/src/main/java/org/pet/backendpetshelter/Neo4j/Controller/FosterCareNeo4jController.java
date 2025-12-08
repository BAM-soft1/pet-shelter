package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.FosterCareNode;
import org.pet.backendpetshelter.Neo4j.Service.FosterCareNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/foster-care")
@CrossOrigin
@Profile("neo4j")
public class FosterCareNeo4jController {

    private final FosterCareNeo4jService fosterCareService;

    public FosterCareNeo4jController(FosterCareNeo4jService fosterCareService) {
        this.fosterCareService = fosterCareService;
    }

    @GetMapping
    public List<FosterCareNode> getAllFosterCares() {
        return fosterCareService.getAllFosterCares();
    }

    @GetMapping("/{id}")
    public FosterCareNode getFosterCareById(@PathVariable String id) {
        return fosterCareService.getFosterCareById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<FosterCareNode> addFosterCare(@RequestBody FosterCareNode fosterCare) {
        return ResponseEntity.status(201).body(fosterCareService.addFosterCare(fosterCare));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FosterCareNode> updateFosterCare(@PathVariable String id, @RequestBody FosterCareNode fosterCare) {
        return ResponseEntity.ok(fosterCareService.updateFosterCare(id, fosterCare));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFosterCare(@PathVariable String id) {
        fosterCareService.deleteFosterCare(id);
        return ResponseEntity.noContent().build();
    }
}
