package org.pet.backendpetshelter.Controller;

import org.pet.backendpetshelter.Service.DogFactService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/dog-facts")
public class DogFactController {
    private final DogFactService dogFactService;

    public DogFactController(DogFactService dogFactService) {
        this.dogFactService = dogFactService;
    }

    @GetMapping
    public List<String> getDogFacts(@RequestParam(defaultValue = "1") int limit) {
        return dogFactService.getDogFacts(limit);
    }
}
