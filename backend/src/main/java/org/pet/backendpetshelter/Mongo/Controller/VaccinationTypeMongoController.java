package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.VaccinationTypeDocument;
import org.pet.backendpetshelter.Mongo.Service.VaccinationTypeMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/vaccination-type")
@CrossOrigin
@Profile("mongo")
public class VaccinationTypeMongoController {

    private final VaccinationTypeMongoService vaccinationTypeService;

    public VaccinationTypeMongoController(VaccinationTypeMongoService vaccinationTypeService) {
        this.vaccinationTypeService = vaccinationTypeService;
    }

    @GetMapping
    public List<VaccinationTypeDocument> getAll() {
        return vaccinationTypeService.getAll();
    }

    @GetMapping("/{id}")
    public VaccinationTypeDocument getById(@PathVariable String id) {
        return vaccinationTypeService.getById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccinationTypeDocument> create(@RequestBody VaccinationTypeDocument vaccinationType) {
        return ResponseEntity.status(201).body(vaccinationTypeService.create(vaccinationType));
    }

    @PutMapping("/update/{id}")
    public VaccinationTypeDocument update(@PathVariable String id, @RequestBody VaccinationTypeDocument vaccinationType) {
        return vaccinationTypeService.update(id, vaccinationType);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vaccinationTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}