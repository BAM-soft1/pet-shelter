package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.Veterinarian;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
    Veterinarian findById(long id);
    Veterinarian findByUser_Email(String email);
    Veterinarian findByUser_UserId(Long userId);
}