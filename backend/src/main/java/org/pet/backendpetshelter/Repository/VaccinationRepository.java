package org.pet.backendpetshelter.Repository;

import org.pet.backendpetshelter.Entity.Vaccination;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    Vaccination findVaccinationById(Long id);
    List<Vaccination> findByAnimalId(Long animalId);
}