package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.AdoptionApplicationDocument;
import org.pet.backendpetshelter.Mongo.Service.AdoptionApplicationMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/adoption-applications")
@CrossOrigin
@Profile("mongo")
public class AdoptionApplicationMongoController {

    private final AdoptionApplicationMongoService applicationService;

    public AdoptionApplicationMongoController(AdoptionApplicationMongoService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public List<AdoptionApplicationDocument> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public AdoptionApplicationDocument getApplicationById(@PathVariable String id) {
        return applicationService.getApplicationById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<AdoptionApplicationDocument> addApplication(@RequestBody AdoptionApplicationDocument application) {
        return ResponseEntity.status(201).body(applicationService.addApplication(application));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdoptionApplicationDocument> updateApplication(@PathVariable String id, @RequestBody AdoptionApplicationDocument application) {
        return ResponseEntity.ok(applicationService.updateApplication(id, application));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
