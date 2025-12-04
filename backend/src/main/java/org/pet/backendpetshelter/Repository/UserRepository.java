package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Entity.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile({"mysql", "migrate-mongo", "migrate-neo4j"})
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
