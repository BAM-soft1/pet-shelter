package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.MedicalRecordNode;
import org.pet.backendpetshelter.Neo4j.Service.MedicalRecordNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/medical-records")
@CrossOrigin
@Profile("neo4j")
public class MedicalRecordNeo4jController {

    private final MedicalRecordNeo4jService medicalRecordService;

    public MedicalRecordNeo4jController(MedicalRecordNeo4jService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecordNode> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/{id}")
    public MedicalRecordNode getMedicalRecordById(@PathVariable String id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<MedicalRecordNode> addMedicalRecord(@RequestBody MedicalRecordNode medicalRecord) {
        return ResponseEntity.status(201).body(medicalRecordService.addMedicalRecord(medicalRecord));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MedicalRecordNode> updateMedicalRecord(@PathVariable String id, @RequestBody MedicalRecordNode medicalRecord) {
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, medicalRecord));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}
