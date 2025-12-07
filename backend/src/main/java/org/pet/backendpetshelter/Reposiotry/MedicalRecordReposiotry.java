package org.pet.backendpetshelter.Reposiotry;

import org.pet.backendpetshelter.Entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordReposiotry extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord findMedicalRecordById(Long id);
    List<MedicalRecord> findByAnimalId(Long animalId);

}