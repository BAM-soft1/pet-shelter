package org.pet.backendpetshelter.Repository;

import org.pet.backendpetshelter.Entity.AdoptionApplication;
import org.pet.backendpetshelter.Status;
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
public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {
    AdoptionApplication findById(long id);
    List<AdoptionApplication> findByUserId(Long userId);

    Boolean existsByUserIdAndAnimalId(Long userId, Long animalId);
    
    // Combined filter method handling all nullable parameters
    @Query("SELECT aa FROM AdoptionApplication aa " +
           "WHERE (:status IS NULL OR aa.status = :status) " +
           "AND (:search IS NULL OR " +
           "LOWER(aa.animal.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(aa.user.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(aa.user.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(aa.user.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<AdoptionApplication> findAllWithFilters(
        @Param("status") Status status,
        @Param("search") String search,
        Pageable pageable
    );
}
