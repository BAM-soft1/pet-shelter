package org.pet.backendpetshelter.Mongo.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@Profile({"mongo", "migrate-mongo"})
public class MongoIndexConfig implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public MongoIndexConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("Setting up MongoDB indexes...");

        // Unique email for users
        mongoTemplate.indexOps("users").ensureIndex(
                new Index().on("email", Sort.Direction.ASC).unique());

        // Index on animal name for search
        mongoTemplate.indexOps("animals").ensureIndex(
                new Index().on("name", Sort.Direction.ASC));

        // Index on animal status for filtering
        mongoTemplate.indexOps("animals").ensureIndex(
                new Index().on("status", Sort.Direction.ASC));

        // Index on species name for filtering
        mongoTemplate.indexOps("animals").ensureIndex(
                new Index().on("species.name", Sort.Direction.ASC));

        // Index on vaccination due dates
        mongoTemplate.indexOps("animals").ensureIndex(
                new Index().on("vaccinations.nextDueDate", Sort.Direction.ASC));

        // Index on adoption application status
        mongoTemplate.indexOps("adoption_applications").ensureIndex(
                new Index().on("status", Sort.Direction.ASC));

        System.out.println("MongoDB indexes complete!");
    }
}