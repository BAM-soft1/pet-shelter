package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.AdoptionDocument;
import org.pet.backendpetshelter.Mongo.Service.AdoptionMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/adoptions")
@CrossOrigin
@Profile("mongo")
public class AdoptionMongoController {

    private final AdoptionMongoService adoptionService;

    public AdoptionMongoController(AdoptionMongoService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping
    public List<AdoptionDocument> getAllAdoptions() {
        return adoptionService.getAllAdoptions();
    }

    @GetMapping("/{id}")
    public AdoptionDocument getAdoptionById(@PathVariable String id) {
        return adoptionService.getAdoptionById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<AdoptionDocument> addAdoption(@RequestBody AdoptionDocument adoption) {
        return ResponseEntity.status(201).body(adoptionService.addAdoption(adoption));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdoptionDocument> updateAdoption(@PathVariable String id, @RequestBody AdoptionDocument adoption) {
        return ResponseEntity.ok(adoptionService.updateAdoption(id, adoption));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable String id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }
}
