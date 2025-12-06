package org.pet.backendpetshelter.Repository;

import org.pet.backendpetshelter.Entity.MedicalRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord findMedicalRecordById(Long id);
}