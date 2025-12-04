package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.VaccineTypeSpeciesNode;
import org.pet.backendpetshelter.Neo4j.Service.VaccineTypeSpeciesNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/vaccine-type-species")
@CrossOrigin
@Profile("neo4j")
public class VaccineTypeSpeciesNeo4jController {

    private final VaccineTypeSpeciesNeo4jService vaccineTypeSpeciesService;

    public VaccineTypeSpeciesNeo4jController(VaccineTypeSpeciesNeo4jService vaccineTypeSpeciesService) {
        this.vaccineTypeSpeciesService = vaccineTypeSpeciesService;
    }

    @GetMapping
    public List<VaccineTypeSpeciesNode> getAllVaccineTypeSpecies() {
        return vaccineTypeSpeciesService.getAllVaccineTypeSpecies();
    }

    @GetMapping("/{id}")
    public VaccineTypeSpeciesNode getVaccineTypeSpeciesById(@PathVariable String id) {
        return vaccineTypeSpeciesService.getVaccineTypeSpeciesById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccineTypeSpeciesNode> addVaccineTypeSpecies(@RequestBody VaccineTypeSpeciesNode vaccineTypeSpecies) {
        return ResponseEntity.status(201).body(vaccineTypeSpeciesService.addVaccineTypeSpecies(vaccineTypeSpecies));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccineTypeSpeciesNode> updateVaccineTypeSpecies(@PathVariable String id, @RequestBody VaccineTypeSpeciesNode vaccineTypeSpecies) {
        return ResponseEntity.ok(vaccineTypeSpeciesService.updateVaccineTypeSpecies(id, vaccineTypeSpecies));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccineTypeSpecies(@PathVariable String id) {
        vaccineTypeSpeciesService.deleteVaccineTypeSpecies(id);
        return ResponseEntity.noContent().build();
    }
}
