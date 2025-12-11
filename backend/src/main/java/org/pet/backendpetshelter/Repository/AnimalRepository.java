package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.Animal;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j", "test" })
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Animal findById(long id);
}
