package org.pet.backendpetshelter.Repository;

import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {
    AdoptionApplication findById(long id);
}
