package org.pet.backendpetshelter.Mongo.Repository;

import org.pet.backendpetshelter.Mongo.Entity.BreedDocument;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("mongo")
public interface BreedMongoRepository extends MongoRepository<BreedDocument, String> {}