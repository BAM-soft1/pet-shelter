package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.VeterinarianNode;
import org.pet.backendpetshelter.Neo4j.Service.VeterinarianNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/veterinarians")
@CrossOrigin
@Profile("neo4j")
public class VeterinarianNeo4jController {

    private final VeterinarianNeo4jService veterinarianService;

    public VeterinarianNeo4jController(VeterinarianNeo4jService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @GetMapping
    public List<VeterinarianNode> getAllVeterinarians() {
        return veterinarianService.getAllVeterinarians();
    }

    @GetMapping("/{id}")
    public VeterinarianNode getVeterinarianById(@PathVariable String id) {
        return veterinarianService.getVeterinarianById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VeterinarianNode> addVeterinarian(@RequestBody VeterinarianNode veterinarian) {
        return ResponseEntity.status(201).body(veterinarianService.addVeterinarian(veterinarian));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VeterinarianNode> updateVeterinarian(@PathVariable String id, @RequestBody VeterinarianNode veterinarian) {
        return ResponseEntity.ok(veterinarianService.updateVeterinarian(id, veterinarian));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable String id) {
        veterinarianService.deleteVeterinarian(id);
        return ResponseEntity.noContent().build();
    }
}
