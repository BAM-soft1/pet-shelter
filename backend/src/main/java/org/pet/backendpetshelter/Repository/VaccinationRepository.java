package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.Vaccination;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    Vaccination findById(long id);
}
