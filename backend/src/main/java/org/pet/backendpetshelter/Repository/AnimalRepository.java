package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.Animal;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Animal findById(long id);
    
    // Combined filter method - using native query only for vaccination check
    @Query(value = "SELECT a.* FROM animal a " +
           "WHERE (:status IS NULL OR a.status = :status) " +
           "AND (:isActive IS NULL OR a.is_active = :isActive) " +
           "AND (:sex IS NULL OR LOWER(a.sex) = LOWER(:sex)) " +
           "AND (:minAge IS NULL OR TIMESTAMPDIFF(YEAR, a.birth_date, CURDATE()) >= :minAge) " +
           "AND (:maxAge IS NULL OR TIMESTAMPDIFF(YEAR, a.birth_date, CURDATE()) <= :maxAge) " +
           "AND (:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:hasRequiredVaccinations IS NULL OR HasRequiredVaccinations(a.animal_id) = :hasRequiredVaccinations)",
           countQuery = "SELECT COUNT(*) FROM animal a " +
           "WHERE (:status IS NULL OR a.status = :status) " +
           "AND (:isActive IS NULL OR a.is_active = :isActive) " +
           "AND (:sex IS NULL OR LOWER(a.sex) = LOWER(:sex)) " +
           "AND (:minAge IS NULL OR TIMESTAMPDIFF(YEAR, a.birth_date, CURDATE()) >= :minAge) " +
           "AND (:maxAge IS NULL OR TIMESTAMPDIFF(YEAR, a.birth_date, CURDATE()) <= :maxAge) " +
           "AND (:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:hasRequiredVaccinations IS NULL OR HasRequiredVaccinations(a.animal_id) = :hasRequiredVaccinations)",
           nativeQuery = true)
    Page<Animal> findAllWithFilters(
        @Param("status") String status,
        @Param("isActive") Boolean isActive,
        @Param("hasRequiredVaccinations") Boolean hasRequiredVaccinations,
        @Param("sex") String sex,
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge,
        @Param("search") String search,
        Pageable pageable
    );
}
