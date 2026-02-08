package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.VaccinationDocument;
import org.pet.backendpetshelter.Mongo.Service.VaccinationMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/vaccination")
@CrossOrigin
@Profile("mongo")
public class VaccinationMongoController {

    private final VaccinationMongoService vaccinationService;

    public VaccinationMongoController(VaccinationMongoService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }

    @GetMapping
    public List<VaccinationDocument> getAll() {
        return vaccinationService.getAll();
    }

    @GetMapping("/{id}")
    public VaccinationDocument getById(@PathVariable String id) {
        return vaccinationService.getById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccinationDocument> create(@RequestBody VaccinationDocument vaccination) {
        return ResponseEntity.status(201).body(vaccinationService.create(vaccination));
    }

    @PutMapping("/update/{id}")
    public VaccinationDocument update(@PathVariable String id, @RequestBody VaccinationDocument vaccination) {
        return vaccinationService.update(id, vaccination);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vaccinationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}