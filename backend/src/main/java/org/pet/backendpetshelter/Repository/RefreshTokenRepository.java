package org.pet.backendpetshelter.Repository;


import org.pet.backendpetshelter.Configuration.RefreshToken;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Profile({"mysql", "migrate-mongo", "migrate-neo4j", "test"})
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(Long userId);
}