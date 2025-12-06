package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.VaccinationTypeNode;
import org.pet.backendpetshelter.Neo4j.Service.VaccinationTypeNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/vaccination-types")
@CrossOrigin
@Profile("neo4j")
public class VaccinationTypeNeo4jController {

    private final VaccinationTypeNeo4jService vaccinationTypeService;

    public VaccinationTypeNeo4jController(VaccinationTypeNeo4jService vaccinationTypeService) {
        this.vaccinationTypeService = vaccinationTypeService;
    }

    @GetMapping
    public List<VaccinationTypeNode> getAllVaccinationTypes() {
        return vaccinationTypeService.getAllVaccinationTypes();
    }

    @GetMapping("/{id}")
    public VaccinationTypeNode getVaccinationTypeById(@PathVariable String id) {
        return vaccinationTypeService.getVaccinationTypeById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccinationTypeNode> addVaccinationType(@RequestBody VaccinationTypeNode vaccinationType) {
        return ResponseEntity.status(201).body(vaccinationTypeService.addVaccinationType(vaccinationType));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccinationTypeNode> updateVaccinationType(@PathVariable String id, @RequestBody VaccinationTypeNode vaccinationType) {
        return ResponseEntity.ok(vaccinationTypeService.updateVaccinationType(id, vaccinationType));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccinationType(@PathVariable String id) {
        vaccinationTypeService.deleteVaccinationType(id);
        return ResponseEntity.noContent().build();
    }
}
