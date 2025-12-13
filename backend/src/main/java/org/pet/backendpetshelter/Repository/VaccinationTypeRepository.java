package org.pet.backendpetshelter.Repository;

import org.pet.backendpetshelter.Entity.VaccinationType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j", "test"})
public interface VaccinationTypeRepository extends JpaRepository<VaccinationType, Long> {
    VaccinationType findById(long id);

    @Query("SELECT vt FROM VaccinationType vt " +
           "WHERE (:requiredForAdoption IS NULL OR vt.requiredForAdoption = :requiredForAdoption) " +
           "AND (:search IS NULL OR LOWER(vt.vaccineName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(vt.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<VaccinationType> findAllWithFilters(
        @Param("requiredForAdoption") Boolean requiredForAdoption,
        @Param("search") String search,
        Pageable pageable
    );
}
