package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.VaccineTypeSpeciesDocument;
import org.pet.backendpetshelter.Mongo.Service.VaccineTypeSpeciesMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/vaccine-type-species")
@CrossOrigin
@Profile("mongo")
public class VaccineTypeSpeciesMongoController {

    private final VaccineTypeSpeciesMongoService service;

    public VaccineTypeSpeciesMongoController(VaccineTypeSpeciesMongoService service) {
        this.service = service;
    }

    @GetMapping
    public List<VaccineTypeSpeciesDocument> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public VaccineTypeSpeciesDocument getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VaccineTypeSpeciesDocument> create(@RequestBody VaccineTypeSpeciesDocument doc) {
        return ResponseEntity.status(201).body(service.create(doc));
    }

    @PutMapping("/update/{id}")
    public VaccineTypeSpeciesDocument update(@PathVariable String id, @RequestBody VaccineTypeSpeciesDocument doc) {
        return service.update(id, doc);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}