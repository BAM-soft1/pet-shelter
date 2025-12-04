package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.Breed;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface BreedRepository extends JpaRepository<Breed, Long> {
    Breed findById(long id);
    Optional<Breed> findByName(String name);


}

