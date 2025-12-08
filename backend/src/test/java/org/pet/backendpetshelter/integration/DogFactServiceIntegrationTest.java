package org.pet.backendpetshelter.integration;

import org.junit.jupiter.api.Test;
import org.pet.backendpetshelter.Service.DogFactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DogFactServiceIntegrationTest {

    @Autowired
    private DogFactService dogFactService;

    @Test
    void shouldFetchDogFactsFromExternalApi() {
        // Act
        List<String> facts = dogFactService.getDogFacts(2);

        // Assert
        assertNotNull(facts);
        assertEquals(2, facts.size(), "Should return exactly 2 facts");
        facts.forEach(fact -> assertFalse(fact.isEmpty(), "Fact should not be empty"));
    }
}
