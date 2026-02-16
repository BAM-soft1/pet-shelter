package org.pet.backendpetshelter.Mongo.Config;

import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;

@Component
@Order(1)
@Profile({"mongo", "migrate-mongo"})
public class MongoSchemaValidator implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public MongoSchemaValidator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        MongoDatabase db = mongoTemplate.getDb();
        System.out.println("Setting up MongoDB schema validation...");

        setupAnimalsValidation(db);
        setupUsersValidation(db);
        setupAdoptionApplicationsValidation(db);
        setupAdoptionsValidation(db);

        System.out.println("MongoDB schema validation complete!");
    }

    private void setupAnimalsValidation(MongoDatabase db) {
        Document validator = Document.parse("""
        {
          "$jsonSchema": {
            "bsonType": "object",
            "required": ["name", "sex", "status", "price", "species"],
            "properties": {
              "name": {
                "bsonType": "string",
                "minLength": 1,
                "maxLength": 80,
                "description": "Animal name is required and max 80 characters"
              },
              "sex": {
                "bsonType": "string",
                "enum": ["male", "female"],
                "description": "Sex must be 'male' or 'female'"
              },
              "status": {
                "bsonType": "string",
                "enum": ["AVAILABLE", "ADOPTED", "FOSTERED", "DECEASED", "PENDING"],
                "description": "Status must be a valid status"
              },
              "price": {
                "bsonType": "double",
                "minimum": 0,
                "description": "Price must be 0 or greater"
              },
              "species": {
                "bsonType": "object",
                "required": ["name"],
                "properties": {
                  "name": {
                    "bsonType": "string",
                    "minLength": 2,
                    "maxLength": 50,
                    "description": "Species name is required"
                  }
                }
              },
              "breed": {
                "bsonType": ["object", "null"],
                "properties": {
                  "name": {
                    "bsonType": "string",
                    "minLength": 3,
                    "maxLength": 50
                  }
                }
              },
              "vaccinations": {
                "bsonType": "array",
                "items": {
                  "bsonType": "object",
                  "required": ["vaccineName", "dateAdministered"],
                  "properties": {
                    "vaccineName": {
                      "bsonType": "string",
                      "minLength": 1,
                      "description": "Vaccine name is required"
                    },
                    "durationMonths": {
                      "bsonType": "int",
                      "minimum": 1,
                      "description": "Duration must be at least 1 month"
                    },
                    "requiredForAdoption": {
                      "bsonType": "bool"
                    },
                    "dateAdministered": {
                      "bsonType": "date",
                      "description": "Date administered is required"
                    },
                    "administeredBy": {
                      "bsonType": "object",
                      "required": ["firstName", "email"],
                      "properties": {
                        "firstName": { "bsonType": "string" },
                        "lastName": { "bsonType": "string" },
                        "email": { "bsonType": "string" }
                      }
                    }
                  }
                }
              },
              "medicalRecords": {
                "bsonType": "array",
                "items": {
                  "bsonType": "object",
                  "required": ["date", "diagnosis"],
                  "properties": {
                    "date": {
                      "bsonType": "date",
                      "description": "Record date is required"
                    },
                    "diagnosis": {
                      "bsonType": "string",
                      "minLength": 1,
                      "description": "Diagnosis is required"
                    },
                    "treatment": {
                      "bsonType": "string"
                    },
                    "cost": {
                      "bsonType": "double",
                      "minimum": 0,
                      "description": "Cost must be 0 or greater"
                    },
                    "veterinarian": {
                      "bsonType": "object",
                      "required": ["firstName", "email"],
                      "properties": {
                        "firstName": { "bsonType": "string" },
                        "lastName": { "bsonType": "string" },
                        "email": { "bsonType": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """);

        applyValidator(db, "animals", validator);
    }

    private void setupUsersValidation(MongoDatabase db) {
        Document validator = Document.parse("""
        {
          "$jsonSchema": {
            "bsonType": "object",
            "required": ["email", "firstName", "lastName", "password", "role"],
            "properties": {
              "email": {
                "bsonType": "string",
                "pattern": "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$",
                "description": "Must be a valid email address"
              },
              "firstName": {
                "bsonType": "string",
                "minLength": 1,
                "maxLength": 50,
                "description": "First name is required"
              },
              "lastName": {
                "bsonType": "string",
                "minLength": 1,
                "maxLength": 50,
                "description": "Last name is required"
              },
              "password": {
                "bsonType": "string",
                "minLength": 4,
                "description": "Password must be at least 4 characters"
              },
              "role": {
                "bsonType": "string",
                "enum": ["ADMIN", "STAFF", "VETERINARIAN", "ADOPTER", "FOSTER", "USER"],
                "description": "Role must be a valid role"
              },
              "phone": {
                "bsonType": ["string", "null"]
              },
              "isActive": {
                "bsonType": "bool"
              }
            }
          }
        }
        """);

        applyValidator(db, "users", validator);
    }

    private void setupAdoptionApplicationsValidation(MongoDatabase db) {
        Document validator = Document.parse("""
        {
          "$jsonSchema": {
            "bsonType": "object",
            "required": ["applicationDate", "status", "applicant", "animal"],
            "properties": {
              "applicationDate": {
                "bsonType": "date",
                "description": "Application date is required"
              },
              "status": {
                "bsonType": "string",
                "enum": ["PENDING", "APPROVED", "REJECTED"],
                "description": "Status must be PENDING, APPROVED, or REJECTED"
              },
              "description": {
                "bsonType": ["string", "null"],
                "maxLength": 1000
              },
              "isActive": {
                "bsonType": "bool"
              },
              "applicant": {
                "bsonType": "object",
                "required": ["name", "email"],
                "properties": {
                  "name": {
                    "bsonType": "string",
                    "minLength": 1,
                    "description": "Applicant name is required"
                  },
                  "email": {
                    "bsonType": "string",
                    "description": "Applicant email is required"
                  },
                  "phone": {
                    "bsonType": ["string", "null"]
                  }
                }
              },
              "animal": {
                "bsonType": "object",
                "required": ["name"],
                "properties": {
                  "name": {
                    "bsonType": "string",
                    "description": "Animal name is required"
                  }
                }
              }
            }
          }
        }
        """);

        applyValidator(db, "adoption_applications", validator);
    }

    private void setupAdoptionsValidation(MongoDatabase db) {
        Document validator = Document.parse("""
        {
          "$jsonSchema": {
            "bsonType": "object",
            "required": ["adoptionDate", "adopter", "animal"],
            "properties": {
              "adoptionDate": {
                "bsonType": "date",
                "description": "Adoption date is required"
              },
              "isActive": {
                "bsonType": "bool"
              },
              "adopter": {
                "bsonType": "object",
                "required": ["name", "email"],
                "properties": {
                  "name": {
                    "bsonType": "string",
                    "minLength": 1,
                    "description": "Adopter name is required"
                  },
                  "email": {
                    "bsonType": "string",
                    "description": "Adopter email is required"
                  },
                  "phone": {
                    "bsonType": ["string", "null"]
                  }
                }
              },
              "animal": {
                "bsonType": "object",
                "required": ["name"],
                "properties": {
                  "name": {
                    "bsonType": "string",
                    "description": "Animal name is required"
                  }
                }
              },
              "application": {
                "bsonType": ["object", "null"],
                "properties": {
                  "status": {
                    "bsonType": "string",
                    "enum": ["PENDING", "APPROVED", "REJECTED"]
                  }
                }
              }
            }
          }
        }
        """);

        applyValidator(db, "adoptions", validator);
    }

    private void applyValidator(MongoDatabase db, String collectionName, Document validator) {
        try {
            // If collection exists, modify it
            if (db.listCollectionNames().into(new java.util.ArrayList<>()).contains(collectionName)) {
                db.runCommand(new Document("collMod", collectionName)
                        .append("validator", validator)
                        .append("validationLevel", "moderate")
                        .append("validationAction", "error"));
            } else {
                // Create collection with validator
                db.createCollection(collectionName, new CreateCollectionOptions()
                        .validationOptions(new ValidationOptions()
                                .validator(validator)
                                .validationLevel(ValidationLevel.MODERATE)
                                .validationAction(ValidationAction.ERROR)));
            }
            System.out.println("  ✓ Validation set for: " + collectionName);
        } catch (Exception e) {
            System.err.println("  ✗ Failed to set validation for " + collectionName + ": " + e.getMessage());
        }
    }
}