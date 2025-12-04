package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.VaccinationNode;
import org.pet.backendpetshelter.Neo4j.Service.VaccinationNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/vaccinations")
@CrossOrigin
@Profile("neo4j")
public class VaccinationNeo4jController {

    private final VaccinationNeo4jService vaccinationService;

    public VaccinationNeo4jController(VaccinationNeo4jService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }

    @GetMapping
    public List<VaccinationNode> getAllVaccinations() {
        return vaccinationService.getAllVaccinations();
    }

    @GetMapping("/{id}")
    public VaccinationNode getVaccinationById(@PathVariable String id) {
        return vaccinationService.getVaccinationById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccinationNode> addVaccination(@RequestBody VaccinationNode vaccination) {
        return ResponseEntity.status(201).body(vaccinationService.addVaccination(vaccination));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccinationNode> updateVaccination(@PathVariable String id, @RequestBody VaccinationNode vaccination) {
        return ResponseEntity.ok(vaccinationService.updateVaccination(id, vaccination));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable String id) {
        vaccinationService.deleteVaccination(id);
        return ResponseEntity.noContent().build();
    }
}
