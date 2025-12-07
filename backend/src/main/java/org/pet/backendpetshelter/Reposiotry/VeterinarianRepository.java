package org.pet.backendpetshelter.Reposiotry;

import org.pet.backendpetshelter.Entity.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
    Veterinarian findById(long id);
    Veterinarian findByUser_Email(String email);
}