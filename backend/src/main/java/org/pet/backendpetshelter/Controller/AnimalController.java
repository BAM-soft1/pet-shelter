package org.pet.backendpetshelter.Controller;


import org.pet.backendpetshelter.DTO.AnimalDTORequest;
import org.pet.backendpetshelter.DTO.AnimalDTOResponse;
import org.pet.backendpetshelter.Service.AnimalService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animal")
@CrossOrigin
@Profile({"mysql", "test"})
public class AnimalController {

    private final AnimalService animalService;


    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }



    @GetMapping
    public Page<AnimalDTOResponse> getAllAnimals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean hasRequiredVaccinations,
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String search) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // If any filters are provided, use filtered query
        if (status != null || isActive != null || hasRequiredVaccinations != null || sex != null || minAge != null || maxAge != null || search != null) {
            return animalService.GetAllAnimalsWithFilters(status, isActive, hasRequiredVaccinations, sex, minAge, maxAge, search, pageable);
        }
        
        return animalService.GetAllAnimals(pageable);
    }


    @GetMapping("/{id}")
    public AnimalDTOResponse getAnimalById(@PathVariable Long id) {
        return animalService.GetAnimalById(id);
    }


    @PostMapping("/add")
    public ResponseEntity<AnimalDTOResponse> addAnimal(@RequestBody AnimalDTORequest animalDTORequest) {
        return ResponseEntity.status(201).body(animalService.addAnimal(animalDTORequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnimalDTOResponse> updateAnimal(@PathVariable Long id, @RequestBody AnimalDTORequest animalDTORequest) {
        AnimalDTOResponse updatedAnimal = animalService.updateAnimal(id, animalDTORequest);
        return ResponseEntity.ok(updatedAnimal);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id){
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }


}
