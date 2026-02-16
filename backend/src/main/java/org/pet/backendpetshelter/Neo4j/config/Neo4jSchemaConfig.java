package org.pet.backendpetshelter.Neo4j.Config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@Profile({"neo4j", "migrate-neo4j"})
public class Neo4jSchemaConfig implements CommandLineRunner {

    private final Driver driver;

    public Neo4jSchemaConfig(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void run(String... args) {
        System.out.println("Setting up Neo4j constraints and indexes...");

        try (Session session = driver.session()) {
            // Unique constraints
            session.run("CREATE CONSTRAINT user_email_unique IF NOT EXISTS FOR (u:User) REQUIRE u.email IS UNIQUE");
            session.run("CREATE CONSTRAINT species_name_unique IF NOT EXISTS FOR (s:Species) REQUIRE s.name IS UNIQUE");
            session.run("CREATE CONSTRAINT breed_name_unique IF NOT EXISTS FOR (b:Breed) REQUIRE b.name IS UNIQUE");
            session.run("CREATE CONSTRAINT vaccination_type_name_unique IF NOT EXISTS FOR (vt:VaccinationType) REQUIRE vt.vaccineName IS UNIQUE");

            // NOT NULL constraints
            session.run("CREATE CONSTRAINT animal_name_not_null IF NOT EXISTS FOR (a:Animal) REQUIRE a.name IS NOT NULL");
            session.run("CREATE CONSTRAINT user_email_not_null IF NOT EXISTS FOR (u:User) REQUIRE u.email IS NOT NULL");
            session.run("CREATE CONSTRAINT species_name_not_null IF NOT EXISTS FOR (s:Species) REQUIRE s.name IS NOT NULL");

            // Performance indexes
            session.run("CREATE INDEX animal_status_index IF NOT EXISTS FOR (a:Animal) ON (a.status)");
            session.run("CREATE INDEX animal_name_index IF NOT EXISTS FOR (a:Animal) ON (a.name)");
            session.run("CREATE INDEX vaccination_date_index IF NOT EXISTS FOR (v:Vaccination) ON (v.dateAdministered)");

            System.out.println("Neo4j constraints and indexes complete!");
        }
    }
}