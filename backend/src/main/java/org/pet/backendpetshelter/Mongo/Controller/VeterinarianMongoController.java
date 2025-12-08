package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.VeterinarianDocument;
import org.pet.backendpetshelter.Mongo.Service.VeterinarianMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/veterinarians")
@CrossOrigin
@Profile("mongo")
public class VeterinarianMongoController {

    private final VeterinarianMongoService veterinarianService;

    public VeterinarianMongoController(VeterinarianMongoService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @GetMapping
    public List<VeterinarianDocument> getAllVeterinarians() {
        return veterinarianService.getAllVeterinarians();
    }

    @GetMapping("/{id}")
    public VeterinarianDocument getVeterinarianById(@PathVariable String id) {
        return veterinarianService.getVeterinarianById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<VeterinarianDocument> addVeterinarian(@RequestBody VeterinarianDocument veterinarian) {
        return ResponseEntity.status(201).body(veterinarianService.addVeterinarian(veterinarian));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VeterinarianDocument> updateVeterinarian(@PathVariable String id, @RequestBody VeterinarianDocument veterinarian) {
        return ResponseEntity.ok(veterinarianService.updateVeterinarian(id, veterinarian));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable String id) {
        veterinarianService.deleteVeterinarian(id);
        return ResponseEntity.noContent().build();
    }
}
