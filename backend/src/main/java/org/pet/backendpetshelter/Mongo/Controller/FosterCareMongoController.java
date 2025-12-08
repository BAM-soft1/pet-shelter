package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.FosterCareDocument;
import org.pet.backendpetshelter.Mongo.Service.FosterCareMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/foster-care")
@CrossOrigin
@Profile("mongo")
public class FosterCareMongoController {

    private final FosterCareMongoService fosterCareService;

    public FosterCareMongoController(FosterCareMongoService fosterCareService) {
        this.fosterCareService = fosterCareService;
    }

    @GetMapping
    public List<FosterCareDocument> getAllFosterCares() {
        return fosterCareService.getAllFosterCares();
    }

    @GetMapping("/{id}")
    public FosterCareDocument getFosterCareById(@PathVariable String id) {
        return fosterCareService.getFosterCareById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<FosterCareDocument> addFosterCare(@RequestBody FosterCareDocument fosterCare) {
        return ResponseEntity.status(201).body(fosterCareService.addFosterCare(fosterCare));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FosterCareDocument> updateFosterCare(@PathVariable String id, @RequestBody FosterCareDocument fosterCare) {
        return ResponseEntity.ok(fosterCareService.updateFosterCare(id, fosterCare));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFosterCare(@PathVariable String id) {
        fosterCareService.deleteFosterCare(id);
        return ResponseEntity.noContent().build();
    }
}
