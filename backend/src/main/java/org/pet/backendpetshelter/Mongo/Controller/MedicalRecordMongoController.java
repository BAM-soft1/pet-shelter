package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.MedicalRecordDocument;
import org.pet.backendpetshelter.Mongo.Service.MedicalRecordMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/medical-records")
@CrossOrigin
@Profile("mongo")
public class MedicalRecordMongoController {

    private final MedicalRecordMongoService medicalRecordService;

    public MedicalRecordMongoController(MedicalRecordMongoService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecordDocument> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/{id}")
    public MedicalRecordDocument getMedicalRecordById(@PathVariable String id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<MedicalRecordDocument> addMedicalRecord(@RequestBody MedicalRecordDocument medicalRecord) {
        return ResponseEntity.status(201).body(medicalRecordService.addMedicalRecord(medicalRecord));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MedicalRecordDocument> updateMedicalRecord(@PathVariable String id, @RequestBody MedicalRecordDocument medicalRecord) {
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, medicalRecord));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}
