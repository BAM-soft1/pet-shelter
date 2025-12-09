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

    @Test
    void shouldFetchSingleDogFact() {
        // Act
        List<String> facts = dogFactService.getDogFacts(1);

        // Assert
        assertNotNull(facts);
        assertEquals(1, facts.size(), "Should return exactly 1 fact");
        assertFalse(facts.get(0).isEmpty(), "Fact should not be empty");
    }

    @Test
    void shouldReturnSingleFactForZeroLimit() {
        // Act
        List<String> facts = dogFactService.getDogFacts(0);

        // Assert
        assertNotNull(facts);
        assertEquals(1, facts.size(), "Should return exactly 1 fact");
        assertFalse(facts.get(0).isEmpty(), "Fact should not be empty");
    }

    @Test
    void shouldHandleMultipleFacts() {
        // Act
        List<String> facts = dogFactService.getDogFacts(5);

        // Assert
        assertNotNull(facts);
        assertTrue(facts.size() <= 5, "Should return at most 5 facts");
        facts.forEach(fact -> {
            assertFalse(fact.isEmpty(), "Each fact should not be empty");
            assertTrue(fact instanceof String, "Each fact should be a String");
        });
    }

    @Test
    void shouldReturnListEvenWithNegativeLimit() {
        // Act
        List<String> facts = dogFactService.getDogFacts(-1);

        // Assert
        assertNotNull(facts);
        // API behavior may vary, but service should not crash
        assertTrue(facts instanceof List, "Should return a List regardless of input");
    }
}
