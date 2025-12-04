package org.pet.backendpetshelter.Mongo.Repository;

import org.pet.backendpetshelter.Mongo.Entity.RefreshTokenDocument;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("mongo")
public interface RefreshTokenMongoRepository extends MongoRepository<RefreshTokenDocument, String> {
    Optional<RefreshTokenDocument> findByToken(String token);
}
