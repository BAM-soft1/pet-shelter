package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.Vaccination;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j", "test" })
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    Vaccination findById(long id);

    @Query(value = "SELECT v.* FROM vaccination v " +
           "LEFT JOIN animal a ON v.animal_id = a.animal_id " +
           "WHERE (:animalStatus IS NULL OR LOWER(a.status) = LOWER(:animalStatus)) " +
           "AND (:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER((SELECT vaccine_name FROM vaccination_type vt WHERE vt.vaccination_type_id = v.vaccination_type_id)) LIKE LOWER(CONCAT('%', :search, '%')))",
           countQuery = "SELECT COUNT(*) FROM vaccination v " +
           "LEFT JOIN animal a ON v.animal_id = a.animal_id " +
           "WHERE (:animalStatus IS NULL OR LOWER(a.status) = LOWER(:animalStatus)) " +
           "AND (:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER((SELECT vaccine_name FROM vaccination_type vt WHERE vt.vaccination_type_id = v.vaccination_type_id)) LIKE LOWER(CONCAT('%', :search, '%')))",
           nativeQuery = true)
    Page<Vaccination> findAllWithFilters(
        @Param("animalStatus") String animalStatus,
        @Param("search") String search,
        Pageable pageable
    );
}