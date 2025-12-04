package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.VaccinationDocument;
import org.pet.backendpetshelter.Mongo.Service.VaccinationMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/vaccinations")
@CrossOrigin
@Profile("mongo")
public class VaccinationMongoController {

    private final VaccinationMongoService vaccinationService;

    public VaccinationMongoController(VaccinationMongoService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }

    @GetMapping
    public List<VaccinationDocument> getAllVaccinations() {
        return vaccinationService.getAllVaccinations();
    }

    @GetMapping("/{id}")
    public VaccinationDocument getVaccinationById(@PathVariable String id) {
        return vaccinationService.getVaccinationById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccinationDocument> addVaccination(@RequestBody VaccinationDocument vaccination) {
        return ResponseEntity.status(201).body(vaccinationService.addVaccination(vaccination));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccinationDocument> updateVaccination(@PathVariable String id, @RequestBody VaccinationDocument vaccination) {
        return ResponseEntity.ok(vaccinationService.updateVaccination(id, vaccination));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable String id) {
        vaccinationService.deleteVaccination(id);
        return ResponseEntity.noContent().build();
    }
}
