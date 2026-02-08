package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.MedicalRecordDocument;
import org.pet.backendpetshelter.Mongo.Service.MedicalRecordMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/medical-record")
@CrossOrigin
@Profile("mongo")
public class MedicalRecordMongoController {

    private final MedicalRecordMongoService medicalRecordService;

    public MedicalRecordMongoController(MedicalRecordMongoService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecordDocument> getAll() {
        return medicalRecordService.getAll();
    }

    @GetMapping("/{id}")
    public MedicalRecordDocument getById(@PathVariable String id) {
        return medicalRecordService.getById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<MedicalRecordDocument> create(@RequestBody MedicalRecordDocument record) {
        return ResponseEntity.status(201).body(medicalRecordService.create(record));
    }

    @PutMapping("/update/{id}")
    public MedicalRecordDocument update(@PathVariable String id, @RequestBody MedicalRecordDocument record) {
        return medicalRecordService.update(id, record);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        medicalRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}