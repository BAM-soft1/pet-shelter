package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.VaccinationTypeDocument;
import org.pet.backendpetshelter.Mongo.Service.VaccinationTypeMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/vaccination-types")
@CrossOrigin
@Profile("mongo")
public class VaccinationTypeMongoController {

    private final VaccinationTypeMongoService vaccinationTypeService;

    public VaccinationTypeMongoController(VaccinationTypeMongoService vaccinationTypeService) {
        this.vaccinationTypeService = vaccinationTypeService;
    }

    @GetMapping
    public List<VaccinationTypeDocument> getAllVaccinationTypes() {
        return vaccinationTypeService.getAllVaccinationTypes();
    }

    @GetMapping("/{id}")
    public VaccinationTypeDocument getVaccinationTypeById(@PathVariable String id) {
        return vaccinationTypeService.getVaccinationTypeById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccinationTypeDocument> addVaccinationType(@RequestBody VaccinationTypeDocument vaccinationType) {
        return ResponseEntity.status(201).body(vaccinationTypeService.addVaccinationType(vaccinationType));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VaccinationTypeDocument> updateVaccinationType(@PathVariable String id, @RequestBody VaccinationTypeDocument vaccinationType) {
        return ResponseEntity.ok(vaccinationTypeService.updateVaccinationType(id, vaccinationType));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVaccinationType(@PathVariable String id) {
        vaccinationTypeService.deleteVaccinationType(id);
        return ResponseEntity.noContent().build();
    }
}
