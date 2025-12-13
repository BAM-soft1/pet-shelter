package org.pet.backendpetshelter.Repository;

import org.pet.backendpetshelter.Entity.MedicalRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j", "test" })
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord findMedicalRecordById(Long id);
    List<MedicalRecord> findByAnimalId(Long animalId);

    @Query("SELECT m FROM MedicalRecord m " +
           "WHERE m.animal IS NOT NULL " +
           "AND (:animalStatus IS NULL OR LOWER(CAST(m.animal.status AS string)) = LOWER(:animalStatus)) " +
           "AND (:startDate IS NULL OR m.date >= :startDate) " +
           "AND (:endDate IS NULL OR m.date <= :endDate) " +
           "AND (:search IS NULL OR LOWER(m.animal.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(m.diagnosis) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(m.treatment) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<MedicalRecord> findAllWithFilters(
        @Param("animalStatus") String animalStatus,
        @Param("startDate") java.util.Date startDate,
        @Param("endDate") java.util.Date endDate,
        @Param("search") String search,
        Pageable pageable
    );
}