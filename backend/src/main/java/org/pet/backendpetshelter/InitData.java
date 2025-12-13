package org.pet.backendpetshelter;


import org.pet.backendpetshelter.Entity.*;
import org.pet.backendpetshelter.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;


@Component
@Order(1) // Run first, before DatabaseFeaturesInitializer
@Profile("(mysql | migrate-mongo | migrate-neo4j) & !test") // Run for mysql and migration profiles
public class InitData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final SpeciesRepository speciesRepository;
    private final BreedRepository breedRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final VaccinationRepository vaccinationRepository;
    private final VaccinationTypeRepository vaccinationTypeRepository;
    private final VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository;
    private final FosterCareRepository fosterCareRepository;
    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final PasswordEncoder passwordEncoder;

    public InitData(UserRepository userRepository, AnimalRepository animalRepository, SpeciesRepository speciesRepository, BreedRepository breedRepository, MedicalRecordRepository medicalRecordRepository, VeterinarianRepository veterinarianRepository, VaccinationRepository vaccinationRepository, VaccinationTypeRepository vaccinationTypeRepository, VaccineTypeSpeciesRepository vaccineTypeSpeciesRepository, FosterCareRepository fosterCareRepository, AdoptionApplicationRepository adoptionApplicationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.speciesRepository = speciesRepository;
        this.breedRepository = breedRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.veterinarianRepository = veterinarianRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
        this.vaccineTypeSpeciesRepository = vaccineTypeSpeciesRepository;
        this.fosterCareRepository = fosterCareRepository;
        this.adoptionApplicationRepository = adoptionApplicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Uncomment the line below to seed the database with initial data
        populateDatabase();
        System.out.println("Application started.");
    }

    public void populateDatabase() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Seeding database with initial data...");

        // Check if database is already seeded
        if (userRepository.findByEmail("ox1@gmail.com").isPresent()) {
            System.out.println("Database already seeded, skipping initialization.");
            return;
        }

        /* Users */
        User user1 = new User();
        user1.setEmail("ox1@gmail.com");
        user1.setPassword(passwordEncoder.encode("test123!"));
        user1.setFirstName("ox");
        user1.setLastName("woo");
        user1.setPhone("424242424");
        user1.setIsActive(true);
        user1.setRole(Roles.ADMIN);


        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("oxVet@gmail.com");
        user2.setPassword(passwordEncoder.encode("123!"));
        user2.setFirstName("oxVet");
        user2.setLastName("wooVet");
        user2.setPhone("424242425");
        user2.setIsActive(true);
        user2.setRole(Roles.VETERINARIAN);
        userRepository.save(user2);


        User user3 = new User();
        user3.setEmail("testuser@mail.com");
        user3.setPassword(passwordEncoder.encode("Testpassword!"));
        user3.setFirstName("Test");
        user3.setLastName("User");
        user3.setPhone("12121212");
        user3.setIsActive(true);
        user3.setRole(Roles.USER);

        userRepository.save(user3);

        User user4 = new User();
        user4.setEmail("oxStaff@mail.com");
        user4.setPassword(passwordEncoder.encode("test123!"));
        user4.setFirstName("Staff");
        user4.setLastName("User");
        user4.setPhone("12121212");
        user4.setIsActive(true);
        user4.setRole(Roles.STAFF);

        userRepository.save(user4);




        Species species1 = new Species();
        species1.setName("Bird");
        Species savedSpecies1 = speciesRepository.save(species1);

        Species species2 = new Species();
        species2.setName("Dog");
        speciesRepository.save(species2);

        Breed breed2 = new Breed();
        breed2.setName("Bulldog");
        breed2.setSpecies(species2);
        breedRepository.save(breed2);

        Species dog = speciesRepository.findByName("Dog")
                .orElseThrow(() -> new RuntimeException("Species 'Dog' not found!"));

        Breed breed = breedRepository.findByName("Bulldog")
                .orElseThrow(() -> new RuntimeException("Breed 'Labrador' not found!"));


        Breed breed1 = new Breed();
        breed1.setName("Parrot");
        breed1.setSpecies(savedSpecies1);
        breedRepository.save(breed1);




        // Create more species and breeds
        Species cat = new Species();
        cat.setName("Cat");
        speciesRepository.save(cat);

        Species rabbit = new Species();
        rabbit.setName("Rabbit");
        speciesRepository.save(rabbit);

        Breed labrador = new Breed();
        labrador.setName("Labrador");
        labrador.setSpecies(species2);
        breedRepository.save(labrador);

        Breed goldenRetriever = new Breed();
        goldenRetriever.setName("Golden Retriever");
        goldenRetriever.setSpecies(species2);
        breedRepository.save(goldenRetriever);

        Breed siamese = new Breed();
        siamese.setName("Siamese");
        siamese.setSpecies(cat);
        breedRepository.save(siamese);

        Breed persian = new Breed();
        persian.setName("Persian");
        persian.setSpecies(cat);
        breedRepository.save(persian);

        Breed dutchRabbit = new Breed();
        dutchRabbit.setName("Dutch");
        dutchRabbit.setSpecies(rabbit);
        breedRepository.save(dutchRabbit);

        // Additional species
        Species hamster = new Species();
        hamster.setName("Hamster");
        speciesRepository.save(hamster);

        Species guineaPig = new Species();
        guineaPig.setName("Guinea Pig");
        speciesRepository.save(guineaPig);

        Species ferret = new Species();
        ferret.setName("Ferret");
        speciesRepository.save(ferret);

        Species turtle = new Species();
        turtle.setName("Turtle");
        speciesRepository.save(turtle);

        // Additional breeds
        Breed germanShepherd = new Breed();
        germanShepherd.setName("German Shepherd");
        germanShepherd.setSpecies(species2);
        breedRepository.save(germanShepherd);

        Breed beagle = new Breed();
        beagle.setName("Beagle");
        beagle.setSpecies(species2);
        breedRepository.save(beagle);

        Breed poodle = new Breed();
        poodle.setName("Poodle");
        poodle.setSpecies(species2);
        breedRepository.save(poodle);

        Breed maineCoon = new Breed();
        maineCoon.setName("Maine Coon");
        maineCoon.setSpecies(cat);
        breedRepository.save(maineCoon);

        Breed ragdoll = new Breed();
        ragdoll.setName("Ragdoll");
        ragdoll.setSpecies(cat);
        breedRepository.save(ragdoll);

        Breed syrianHamster = new Breed();
        syrianHamster.setName("Syrian");
        syrianHamster.setSpecies(hamster);
        breedRepository.save(syrianHamster);

        Breed americanGuineaPig = new Breed();
        americanGuineaPig.setName("American");
        americanGuineaPig.setSpecies(guineaPig);
        breedRepository.save(americanGuineaPig);

        Breed marshallFerret = new Breed();
        marshallFerret.setName("Marshall");
        marshallFerret.setSpecies(ferret);
        breedRepository.save(marshallFerret);

        Breed redEaredSlider = new Breed();
        redEaredSlider.setName("Red-Eared Slider");
        redEaredSlider.setSpecies(turtle);
        breedRepository.save(redEaredSlider);

        Breed macaw = new Breed();
        macaw.setName("Macaw");
        macaw.setSpecies(savedSpecies1);
        breedRepository.save(macaw);

        Breed cockatiel = new Breed();
        cockatiel.setName("Cockatiel");
        cockatiel.setSpecies(savedSpecies1);
        breedRepository.save(cockatiel);

        Breed hollandLop = new Breed();
        hollandLop.setName("Holland Lop");
        hollandLop.setSpecies(rabbit);
        breedRepository.save(hollandLop);


        // Animal 1: Rex (Bulldog)
        Animal animal1 = new Animal();
        animal1.setName("Rex");
        animal1.setSpecies(dog);
        animal1.setBreed(breed);
        animal1.setBirthDate(dateFormat.parse("2021-03-14"));
        animal1.setSex("male");
        animal1.setIntakeDate(dateFormat.parse("2022-06-27"));
        animal1.setStatus(Status.AVAILABLE);
        animal1.setPrice(250);
        animal1.setIsActive(true);
        animal1.setImageUrl("https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=800");
        animalRepository.save(animal1);

        // Animal 2: Polly (Parrot)
        Animal animal2 = new Animal();
        animal2.setName("Polly");
        animal2.setSpecies(savedSpecies1);
        animal2.setBreed(breed1);
        animal2.setBirthDate(dateFormat.parse("2020-05-20"));
        animal2.setSex("female");
        animal2.setIntakeDate(dateFormat.parse("2023-01-15"));
        animal2.setStatus(Status.AVAILABLE);
        animal2.setPrice(150);
        animal2.setIsActive(true);
        animal2.setImageUrl("https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=800");
        animalRepository.save(animal2);

        // Animal 3: Max (Labrador)
        Animal animal3 = new Animal();
        animal3.setName("Max");
        animal3.setSpecies(dog);
        animal3.setBreed(labrador);
        animal3.setBirthDate(dateFormat.parse("2019-08-10"));
        animal3.setSex("male");
        animal3.setIntakeDate(dateFormat.parse("2023-03-20"));
        animal3.setStatus(Status.AVAILABLE);
        animal3.setPrice(300);
        animal3.setIsActive(true);
        animal3.setImageUrl("https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=800");
        animalRepository.save(animal3);

        // Animal 4: Luna (Siamese Cat)
        Animal animal4 = new Animal();
        animal4.setName("Luna");
        animal4.setSpecies(cat);
        animal4.setBreed(siamese);
        animal4.setBirthDate(dateFormat.parse("2022-01-15"));
        animal4.setSex("female");
        animal4.setIntakeDate(dateFormat.parse("2023-05-10"));
        animal4.setStatus(Status.AVAILABLE);
        animal4.setPrice(200);
        animal4.setIsActive(true);
        animal4.setImageUrl("https://images.unsplash.com/photo-1513360371669-4adf3dd7dff8?w=800");
        animalRepository.save(animal4);

        // Animal 5: Bella (Golden Retriever)
        Animal animal5 = new Animal();
        animal5.setName("Bella");
        animal5.setSpecies(dog);
        animal5.setBreed(goldenRetriever);
        animal5.setBirthDate(dateFormat.parse("2020-11-05"));
        animal5.setSex("female");
        animal5.setIntakeDate(dateFormat.parse("2023-07-22"));
        animal5.setStatus(Status.AVAILABLE);
        animal5.setPrice(350);
        animal5.setIsActive(true);
        animal5.setImageUrl("https://images.unsplash.com/photo-1633722715463-d30f4f325e24?w=800");
        animalRepository.save(animal5);

        // Animal 6: Whiskers (Persian Cat)
        Animal animal6 = new Animal();
        animal6.setName("Whiskers");
        animal6.setSpecies(cat);
        animal6.setBreed(persian);
        animal6.setBirthDate(dateFormat.parse("2021-06-18"));
        animal6.setSex("male");
        animal6.setIntakeDate(dateFormat.parse("2023-08-05"));
        animal6.setStatus(Status.AVAILABLE);
        animal6.setPrice(250);
        animal6.setIsActive(true);
        animal6.setImageUrl("https://images.unsplash.com/photo-1495360010541-f48722b34f7d?w=800");
        animalRepository.save(animal6);

        // Animal 7: Charlie (Dog - no specific breed)
        Animal animal7 = new Animal();
        animal7.setName("Charlie");
        animal7.setSpecies(dog);
        animal7.setBreed(null);
        animal7.setBirthDate(dateFormat.parse("2018-04-22"));
        animal7.setSex("male");
        animal7.setIntakeDate(dateFormat.parse("2023-09-12"));
        animal7.setStatus(Status.AVAILABLE);
        animal7.setPrice(200);
        animal7.setIsActive(true);
        animal7.setImageUrl("https://images.unsplash.com/photo-1568572933382-74d440642117?w=800");
        animalRepository.save(animal7);

        // Animal 8: Snowball (Dutch Rabbit)
        Animal animal8 = new Animal();
        animal8.setName("Snowball");
        animal8.setSpecies(rabbit);
        animal8.setBreed(dutchRabbit);
        animal8.setBirthDate(dateFormat.parse("2023-02-10"));
        animal8.setSex("female");
        animal8.setIntakeDate(dateFormat.parse("2023-10-01"));
        animal8.setStatus(Status.AVAILABLE);
        animal8.setPrice(100);
        animal8.setIsActive(true);
        animal8.setImageUrl("https://images.unsplash.com/photo-1585110396000-c9ffd4e4b308?w=800");
        animalRepository.save(animal8);

        // Animal 9: Mittens (Cat - no specific breed)
        Animal animal9 = new Animal();
        animal9.setName("Mittens");
        animal9.setSpecies(cat);
        animal9.setBreed(null);
        animal9.setBirthDate(dateFormat.parse("2022-07-30"));
        animal9.setSex("female");
        animal9.setIntakeDate(dateFormat.parse("2023-10-15"));
        animal9.setStatus(Status.AVAILABLE);
        animal9.setPrice(150);
        animal9.setIsActive(true);
        animal9.setImageUrl("https://images.unsplash.com/photo-1574158622682-e40e69881006?w=800");
        animalRepository.save(animal9);

        // Animal 10: Buddy (Labrador - adopted)
        Animal animal10 = new Animal();
        animal10.setName("Buddy");
        animal10.setSpecies(dog);
        animal10.setBreed(labrador);
        animal10.setBirthDate(dateFormat.parse("2019-12-01"));
        animal10.setSex("male");
        animal10.setIntakeDate(dateFormat.parse("2023-01-05"));
        animal10.setStatus(Status.ADOPTED);
        animal10.setPrice(300);
        animal10.setIsActive(false);
        animal10.setImageUrl("https://images.unsplash.com/photo-1558788353-f76d92427f16?w=800");
        animalRepository.save(animal10);

        // Animal 11: Rocky (Bulldog)
        Animal animal11 = new Animal();
        animal11.setName("Rocky");
        animal11.setSpecies(dog);
        animal11.setBreed(breed);
        animal11.setBirthDate(dateFormat.parse("2021-09-15"));
        animal11.setSex("male");
        animal11.setIntakeDate(dateFormat.parse("2023-11-01"));
        animal11.setStatus(Status.AVAILABLE);
        animal11.setPrice(275);
        animal11.setIsActive(true);
        animal11.setImageUrl("https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=800");
        animalRepository.save(animal11);

        // Animal 12: Daisy (Golden Retriever)
        Animal animal12 = new Animal();
        animal12.setName("Daisy");
        animal12.setSpecies(dog);
        animal12.setBreed(goldenRetriever);
        animal12.setBirthDate(dateFormat.parse("2022-03-22"));
        animal12.setSex("female");
        animal12.setIntakeDate(dateFormat.parse("2023-11-10"));
        animal12.setStatus(Status.AVAILABLE);
        animal12.setPrice(325);
        animal12.setIsActive(true);
        animal12.setImageUrl("https://images.unsplash.com/photo-1633722715463-d30f4f325e24?w=800");
        animalRepository.save(animal12);

        // Animal 13: Shadow (Persian Cat)
        Animal animal13 = new Animal();
        animal13.setName("Shadow");
        animal13.setSpecies(cat);
        animal13.setBreed(persian);
        animal13.setBirthDate(dateFormat.parse("2021-11-20"));
        animal13.setSex("male");
        animal13.setIntakeDate(dateFormat.parse("2023-11-15"));
        animal13.setStatus(Status.AVAILABLE);
        animal13.setPrice(225);
        animal13.setIsActive(true);
        animal13.setImageUrl("https://images.unsplash.com/photo-1595433707802-6b2626ef1c91?w=800");
        animalRepository.save(animal13);

        // Animal 14: Chloe (Siamese Cat)
        Animal animal14 = new Animal();
        animal14.setName("Chloe");
        animal14.setSpecies(cat);
        animal14.setBreed(siamese);
        animal14.setBirthDate(dateFormat.parse("2022-05-10"));
        animal14.setSex("female");
        animal14.setIntakeDate(dateFormat.parse("2023-11-20"));
        animal14.setStatus(Status.AVAILABLE);
        animal14.setPrice(210);
        animal14.setIsActive(true);
        animal14.setImageUrl("https://images.unsplash.com/photo-1606214174585-fe31582dc6ee?w=800");
        animalRepository.save(animal14);

        // Animal 15: Duke (Labrador)
        Animal animal15 = new Animal();
        animal15.setName("Duke");
        animal15.setSpecies(dog);
        animal15.setBreed(labrador);
        animal15.setBirthDate(dateFormat.parse("2020-07-18"));
        animal15.setSex("male");
        animal15.setIntakeDate(dateFormat.parse("2023-11-25"));
        animal15.setStatus(Status.AVAILABLE);
        animal15.setPrice(290);
        animal15.setIsActive(true);
        animal15.setImageUrl("https://images.unsplash.com/photo-1552053831-71594a27632d?w=800");
        animalRepository.save(animal15);

        // Animal 16: Thumper (Dutch Rabbit)
        Animal animal16 = new Animal();
        animal16.setName("Thumper");
        animal16.setSpecies(rabbit);
        animal16.setBreed(dutchRabbit);
        animal16.setBirthDate(dateFormat.parse("2023-04-05"));
        animal16.setSex("male");
        animal16.setIntakeDate(dateFormat.parse("2023-11-28"));
        animal16.setStatus(Status.AVAILABLE);
        animal16.setPrice(110);
        animal16.setIsActive(true);
        animal16.setImageUrl("https://images.unsplash.com/photo-1535241749838-299277b6305f?w=800");
        animalRepository.save(animal16);

        // Animal 17: Kiwi (Parrot)
        Animal animal17 = new Animal();
        animal17.setName("Kiwi");
        animal17.setSpecies(savedSpecies1);
        animal17.setBreed(breed1);
        animal17.setBirthDate(dateFormat.parse("2021-02-14"));
        animal17.setSex("male");
        animal17.setIntakeDate(dateFormat.parse("2023-12-01"));
        animal17.setStatus(Status.AVAILABLE);
        animal17.setPrice(160);
        animal17.setIsActive(true);
        animal17.setImageUrl("https://images.unsplash.com/photo-1544923408-75c5cef46f14?w=800");
        animalRepository.save(animal17);

        // Animal 18: Molly (Golden Retriever)
        Animal animal18 = new Animal();
        animal18.setName("Molly");
        animal18.setSpecies(dog);
        animal18.setBreed(goldenRetriever);
        animal18.setBirthDate(dateFormat.parse("2021-10-30"));
        animal18.setSex("female");
        animal18.setIntakeDate(dateFormat.parse("2023-12-03"));
        animal18.setStatus(Status.AVAILABLE);
        animal18.setPrice(340);
        animal18.setIsActive(true);
        animal18.setImageUrl("https://images.unsplash.com/photo-1612536979211-00c9eef2e9cc?w=800");
        animalRepository.save(animal18);

        // Animal 19: Oliver (Cat - mixed breed)
        Animal animal19 = new Animal();
        animal19.setName("Oliver");
        animal19.setSpecies(cat);
        animal19.setBreed(null);
        animal19.setBirthDate(dateFormat.parse("2022-08-25"));
        animal19.setSex("male");
        animal19.setIntakeDate(dateFormat.parse("2023-12-05"));
        animal19.setStatus(Status.AVAILABLE);
        animal19.setPrice(140);
        animal19.setIsActive(true);
        animal19.setImageUrl("https://images.unsplash.com/photo-1519052537078-e6302a4968d4?w=800");
        animalRepository.save(animal19);

        // Animal 20: Sadie (Labrador)
        Animal animal20 = new Animal();
        animal20.setName("Sadie");
        animal20.setSpecies(dog);
        animal20.setBreed(labrador);
        animal20.setBirthDate(dateFormat.parse("2020-12-12"));
        animal20.setSex("female");
        animal20.setIntakeDate(dateFormat.parse("2023-12-08"));
        animal20.setStatus(Status.AVAILABLE);
        animal20.setPrice(310);
        animal20.setIsActive(true);
        animal20.setImageUrl("https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=800");
        animalRepository.save(animal20);

        // Animal 21: Zeus (German Shepherd)
        Animal animal21 = new Animal();
        animal21.setName("Zeus");
        animal21.setSpecies(dog);
        animal21.setBreed(germanShepherd);
        animal21.setBirthDate(dateFormat.parse("2021-01-20"));
        animal21.setSex("male");
        animal21.setIntakeDate(dateFormat.parse("2023-12-10"));
        animal21.setStatus(Status.AVAILABLE);
        animal21.setPrice(380);
        animal21.setIsActive(true);
        animal21.setImageUrl("https://images.unsplash.com/photo-1568572933382-74d440642117?w=800");
        animalRepository.save(animal21);

        // Animal 22: Sophie (Beagle)
        Animal animal22 = new Animal();
        animal22.setName("Sophie");
        animal22.setSpecies(dog);
        animal22.setBreed(beagle);
        animal22.setBirthDate(dateFormat.parse("2022-06-15"));
        animal22.setSex("female");
        animal22.setIntakeDate(dateFormat.parse("2023-12-11"));
        animal22.setStatus(Status.AVAILABLE);
        animal22.setPrice(260);
        animal22.setIsActive(true);
        animal22.setImageUrl("https://images.unsplash.com/photo-1505628346881-b72b27e84530?w=800");
        animalRepository.save(animal22);

        // Animal 23: Teddy (Poodle)
        Animal animal23 = new Animal();
        animal23.setName("Teddy");
        animal23.setSpecies(dog);
        animal23.setBreed(poodle);
        animal23.setBirthDate(dateFormat.parse("2021-09-08"));
        animal23.setSex("male");
        animal23.setIntakeDate(dateFormat.parse("2023-12-12"));
        animal23.setStatus(Status.AVAILABLE);
        animal23.setPrice(320);
        animal23.setIsActive(true);
        animal23.setImageUrl("https://images.unsplash.com/photo-1544717302-de2939b7ef71?w=800");
        animalRepository.save(animal23);

        // Animal 24: Simba (Maine Coon)
        Animal animal24 = new Animal();
        animal24.setName("Simba");
        animal24.setSpecies(cat);
        animal24.setBreed(maineCoon);
        animal24.setBirthDate(dateFormat.parse("2021-03-12"));
        animal24.setSex("male");
        animal24.setIntakeDate(dateFormat.parse("2023-12-13"));
        animal24.setStatus(Status.AVAILABLE);
        animal24.setPrice(280);
        animal24.setIsActive(true);
        animal24.setImageUrl("https://images.unsplash.com/photo-1548681528-6a5c45b66b42?w=800");
        animalRepository.save(animal24);

        // Animal 25: Princess (Ragdoll)
        Animal animal25 = new Animal();
        animal25.setName("Princess");
        animal25.setSpecies(cat);
        animal25.setBreed(ragdoll);
        animal25.setBirthDate(dateFormat.parse("2022-02-28"));
        animal25.setSex("female");
        animal25.setIntakeDate(dateFormat.parse("2023-11-30"));
        animal25.setStatus(Status.AVAILABLE);
        animal25.setPrice(270);
        animal25.setIsActive(true);
        animal25.setImageUrl("https://images.unsplash.com/photo-1573865526739-10c1d3a1f0cc?w=800");
        animalRepository.save(animal25);

        // Animal 26: Peanut (Syrian Hamster)
        Animal animal26 = new Animal();
        animal26.setName("Peanut");
        animal26.setSpecies(hamster);
        animal26.setBreed(syrianHamster);
        animal26.setBirthDate(dateFormat.parse("2023-08-01"));
        animal26.setSex("male");
        animal26.setIntakeDate(dateFormat.parse("2023-12-01"));
        animal26.setStatus(Status.AVAILABLE);
        animal26.setPrice(30);
        animal26.setIsActive(true);
        animal26.setImageUrl("https://images.unsplash.com/photo-1425082661705-1834bfd09dca?w=800");
        animalRepository.save(animal26);

        // Animal 27: Oreo (American Guinea Pig)
        Animal animal27 = new Animal();
        animal27.setName("Oreo");
        animal27.setSpecies(guineaPig);
        animal27.setBreed(americanGuineaPig);
        animal27.setBirthDate(dateFormat.parse("2023-05-20"));
        animal27.setSex("male");
        animal27.setIntakeDate(dateFormat.parse("2023-12-02"));
        animal27.setStatus(Status.AVAILABLE);
        animal27.setPrice(45);
        animal27.setIsActive(true);
        animal27.setImageUrl("https://images.unsplash.com/photo-1548767797-d8c844163c4c?w=800");
        animalRepository.save(animal27);

        // Animal 28: Bandit (Marshall Ferret)
        Animal animal28 = new Animal();
        animal28.setName("Bandit");
        animal28.setSpecies(ferret);
        animal28.setBreed(marshallFerret);
        animal28.setBirthDate(dateFormat.parse("2022-11-10"));
        animal28.setSex("male");
        animal28.setIntakeDate(dateFormat.parse("2023-12-03"));
        animal28.setStatus(Status.AVAILABLE);
        animal28.setPrice(180);
        animal28.setIsActive(true);
        animal28.setImageUrl("https://images.unsplash.com/photo-1535241749838-299277b6305f?w=800");
        animalRepository.save(animal28);

        // Animal 29: Shelly (Red-Eared Slider Turtle)
        Animal animal29 = new Animal();
        animal29.setName("Shelly");
        animal29.setSpecies(turtle);
        animal29.setBreed(redEaredSlider);
        animal29.setBirthDate(dateFormat.parse("2021-07-04"));
        animal29.setSex("female");
        animal29.setIntakeDate(dateFormat.parse("2023-12-04"));
        animal29.setStatus(Status.AVAILABLE);
        animal29.setPrice(90);
        animal29.setIsActive(true);
        animal29.setImageUrl("https://images.unsplash.com/photo-1437622368342-7a3d73a34c8f?w=800");
        animalRepository.save(animal29);

        // Animal 30: Rio (Macaw)
        Animal animal30 = new Animal();
        animal30.setName("Rio");
        animal30.setSpecies(savedSpecies1);
        animal30.setBreed(macaw);
        animal30.setBirthDate(dateFormat.parse("2020-03-25"));
        animal30.setSex("male");
        animal30.setIntakeDate(dateFormat.parse("2023-12-05"));
        animal30.setStatus(Status.AVAILABLE);
        animal30.setPrice(450);
        animal30.setIsActive(true);
        animal30.setImageUrl("https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=800");
        animalRepository.save(animal30);

        // Animal 31: Sunny (Cockatiel)
        Animal animal31 = new Animal();
        animal31.setName("Sunny");
        animal31.setSpecies(savedSpecies1);
        animal31.setBreed(cockatiel);
        animal31.setBirthDate(dateFormat.parse("2022-09-14"));
        animal31.setSex("female");
        animal31.setIntakeDate(dateFormat.parse("2023-12-06"));
        animal31.setStatus(Status.AVAILABLE);
        animal31.setPrice(120);
        animal31.setIsActive(true);
        animal31.setImageUrl("https://images.unsplash.com/photo-1571667774695-4afcd8e8c3ba?w=800");
        animalRepository.save(animal31);

        // Animal 32: Clover (Holland Lop Rabbit)
        Animal animal32 = new Animal();
        animal32.setName("Clover");
        animal32.setSpecies(rabbit);
        animal32.setBreed(hollandLop);
        animal32.setBirthDate(dateFormat.parse("2023-03-18"));
        animal32.setSex("female");
        animal32.setIntakeDate(dateFormat.parse("2023-12-07"));
        animal32.setStatus(Status.AVAILABLE);
        animal32.setPrice(105);
        animal32.setIsActive(true);
        animal32.setImageUrl("https://images.unsplash.com/photo-1585110396000-c9ffd4e4b308?w=800");
        animalRepository.save(animal32);

        // Animal 33: Cooper (Beagle)
        Animal animal33 = new Animal();
        animal33.setName("Cooper");
        animal33.setSpecies(dog);
        animal33.setBreed(beagle);
        animal33.setBirthDate(dateFormat.parse("2020-10-22"));
        animal33.setSex("male");
        animal33.setIntakeDate(dateFormat.parse("2023-12-08"));
        animal33.setStatus(Status.AVAILABLE);
        animal33.setPrice(275);
        animal33.setIsActive(true);
        animal33.setImageUrl("https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=800");
        animalRepository.save(animal33);

        // Animal 34: Nala (Ragdoll)
        Animal animal34 = new Animal();
        animal34.setName("Nala");
        animal34.setSpecies(cat);
        animal34.setBreed(ragdoll);
        animal34.setBirthDate(dateFormat.parse("2021-12-05"));
        animal34.setSex("female");
        animal34.setIntakeDate(dateFormat.parse("2023-12-09"));
        animal34.setStatus(Status.AVAILABLE);
        animal34.setPrice(265);
        animal34.setIsActive(true);
        animal34.setImageUrl("https://images.unsplash.com/photo-1574158622682-e40e69881006?w=800");
        animalRepository.save(animal34);

        // Animal 35: Ginger (American Guinea Pig)
        Animal animal35 = new Animal();
        animal35.setName("Ginger");
        animal35.setSpecies(guineaPig);
        animal35.setBreed(americanGuineaPig);
        animal35.setBirthDate(dateFormat.parse("2023-06-12"));
        animal35.setSex("female");
        animal35.setIntakeDate(dateFormat.parse("2023-12-10"));
        animal35.setStatus(Status.AVAILABLE);
        animal35.setPrice(40);
        animal35.setIsActive(true);
        animal35.setImageUrl("https://images.unsplash.com/photo-1548767797-d8c844163c4c?w=800");
        animalRepository.save(animal35);


        /* Veterinarian */
        Veterinarian veterinarian1 = new Veterinarian();
        veterinarian1.setUser(user2);
        veterinarian1.setLicenseNumber("VET123456");
        veterinarian1.setClinicName("Happy Pets Clinic");
        veterinarian1.setIsActive(true);
        veterinarianRepository.save(veterinarian1);


        /* Medical Records */
        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setAnimal(animal1);
        medicalRecord1.setVeterinarian(veterinarian1);
        medicalRecord1.setDate(dateFormat.parse("2023-07-10"));
        medicalRecord1.setDiagnosis("Regular Checkup - Healthy");
        medicalRecord1.setTreatment("N/A");
        medicalRecord1.setCost(50);
        medicalRecordRepository.save(medicalRecord1);

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setAnimal(animal2);
        medicalRecord2.setVeterinarian(veterinarian1);
        medicalRecord2.setDate(dateFormat.parse("2023-08-05"));
        medicalRecord2.setDiagnosis("Wing Injury");
        medicalRecord2.setTreatment("Bandaged wing, prescribed antibiotics");
        medicalRecord2.setCost(120);
        medicalRecordRepository.save(medicalRecord2);



        /* Vaccination Types */
        VaccinationType rabiesVaccine = new VaccinationType();
        rabiesVaccine.setVaccineName("Rabies Vaccine");
        rabiesVaccine.setDescription("Protects against rabies virus.");
        rabiesVaccine.setDurationMonths(12);
        rabiesVaccine.setRequiredForAdoption(true);
        vaccinationTypeRepository.save(rabiesVaccine);

        VaccinationType dhppVaccine = new VaccinationType();
        dhppVaccine.setVaccineName("DHPP Vaccine");
        dhppVaccine.setDescription("Protects against Distemper, Hepatitis, Parvovirus, and Parainfluenza.");
        dhppVaccine.setDurationMonths(12);
        dhppVaccine.setRequiredForAdoption(true);
        vaccinationTypeRepository.save(dhppVaccine);

        VaccinationType fvrcpVaccine = new VaccinationType();
        fvrcpVaccine.setVaccineName("FVRCP Vaccine");
        fvrcpVaccine.setDescription("Protects against Feline Viral Rhinotracheitis, Calicivirus, and Panleukopenia.");
        fvrcpVaccine.setDurationMonths(12);
        fvrcpVaccine.setRequiredForAdoption(true);
        vaccinationTypeRepository.save(fvrcpVaccine);

        VaccinationType rhdvVaccine = new VaccinationType();
        rhdvVaccine.setVaccineName("RHDV Vaccine");
        rhdvVaccine.setDescription("Protects against Rabbit Hemorrhagic Disease Virus.");
        rhdvVaccine.setDurationMonths(12);
        rhdvVaccine.setRequiredForAdoption(true);
        vaccinationTypeRepository.save(rhdvVaccine);

        VaccinationType avianVaccine = new VaccinationType();
        avianVaccine.setVaccineName("Avian Polyomavirus Vaccine");
        avianVaccine.setDescription("Protects against Avian Polyomavirus.");
        avianVaccine.setDurationMonths(12);
        avianVaccine.setRequiredForAdoption(true);
        vaccinationTypeRepository.save(avianVaccine);


        /* Vaccine Type Species Mappings */
        VaccineTypeSpecies vts1 = new VaccineTypeSpecies();
        vts1.setSpecies(dog);
        vts1.setVaccinationType(rabiesVaccine);
        vaccineTypeSpeciesRepository.save(vts1);

        VaccineTypeSpecies vts2 = new VaccineTypeSpecies();
        vts2.setSpecies(dog);
        vts2.setVaccinationType(dhppVaccine);
        vaccineTypeSpeciesRepository.save(vts2);

        VaccineTypeSpecies vts3 = new VaccineTypeSpecies();
        vts3.setSpecies(cat);
        vts3.setVaccinationType(rabiesVaccine);
        vaccineTypeSpeciesRepository.save(vts3);

        VaccineTypeSpecies vts4 = new VaccineTypeSpecies();
        vts4.setSpecies(cat);
        vts4.setVaccinationType(fvrcpVaccine);
        vaccineTypeSpeciesRepository.save(vts4);

        VaccineTypeSpecies vts5 = new VaccineTypeSpecies();
        vts5.setSpecies(rabbit);
        vts5.setVaccinationType(rhdvVaccine);
        vaccineTypeSpeciesRepository.save(vts5);

        VaccineTypeSpecies vts6 = new VaccineTypeSpecies();
        vts6.setSpecies(savedSpecies1); // Bird
        vts6.setVaccinationType(avianVaccine);
        vaccineTypeSpeciesRepository.save(vts6);


        /* Vaccinations for Animals */
        // Animal 1 - Rex (Dog)
        Vaccination vaccination1 = new Vaccination();
        vaccination1.setAnimal(animal1);
        vaccination1.setVeterinarian(veterinarian1);
        vaccination1.setVaccinationType(rabiesVaccine);
        vaccination1.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination1.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination1);

        Vaccination vaccination1b = new Vaccination();
        vaccination1b.setAnimal(animal1);
        vaccination1b.setVeterinarian(veterinarian1);
        vaccination1b.setVaccinationType(dhppVaccine);
        vaccination1b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination1b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination1b);

        // Animal 2 - Polly (Bird)
        Vaccination vaccination2 = new Vaccination();
        vaccination2.setAnimal(animal2);
        vaccination2.setVeterinarian(veterinarian1);
        vaccination2.setVaccinationType(avianVaccine);
        vaccination2.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination2.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination2);

        // Animal 3 - Max (Dog)
        Vaccination vaccination3a = new Vaccination();
        vaccination3a.setAnimal(animal3);
        vaccination3a.setVeterinarian(veterinarian1);
        vaccination3a.setVaccinationType(rabiesVaccine);
        vaccination3a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination3a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination3a);

        Vaccination vaccination3b = new Vaccination();
        vaccination3b.setAnimal(animal3);
        vaccination3b.setVeterinarian(veterinarian1);
        vaccination3b.setVaccinationType(dhppVaccine);
        vaccination3b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination3b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination3b);

        // Animal 4 - Luna (Cat)
        Vaccination vaccination4a = new Vaccination();
        vaccination4a.setAnimal(animal4);
        vaccination4a.setVeterinarian(veterinarian1);
        vaccination4a.setVaccinationType(rabiesVaccine);
        vaccination4a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination4a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination4a);

        Vaccination vaccination4b = new Vaccination();
        vaccination4b.setAnimal(animal4);
        vaccination4b.setVeterinarian(veterinarian1);
        vaccination4b.setVaccinationType(fvrcpVaccine);
        vaccination4b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination4b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination4b);

        // Animal 5 - Bella (Dog)
        Vaccination vaccination5a = new Vaccination();
        vaccination5a.setAnimal(animal5);
        vaccination5a.setVeterinarian(veterinarian1);
        vaccination5a.setVaccinationType(rabiesVaccine);
        vaccination5a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination5a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination5a);

        Vaccination vaccination5b = new Vaccination();
        vaccination5b.setAnimal(animal5);
        vaccination5b.setVeterinarian(veterinarian1);
        vaccination5b.setVaccinationType(dhppVaccine);
        vaccination5b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination5b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination5b);

        // Animal 6 - Whiskers (Cat)
        Vaccination vaccination6a = new Vaccination();
        vaccination6a.setAnimal(animal6);
        vaccination6a.setVeterinarian(veterinarian1);
        vaccination6a.setVaccinationType(rabiesVaccine);
        vaccination6a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination6a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination6a);

        Vaccination vaccination6b = new Vaccination();
        vaccination6b.setAnimal(animal6);
        vaccination6b.setVeterinarian(veterinarian1);
        vaccination6b.setVaccinationType(fvrcpVaccine);
        vaccination6b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination6b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination6b);

        // Animal 7 - Charlie (Dog)
        Vaccination vaccination7a = new Vaccination();
        vaccination7a.setAnimal(animal7);
        vaccination7a.setVeterinarian(veterinarian1);
        vaccination7a.setVaccinationType(rabiesVaccine);
        vaccination7a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination7a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination7a);

        Vaccination vaccination7b = new Vaccination();
        vaccination7b.setAnimal(animal7);
        vaccination7b.setVeterinarian(veterinarian1);
        vaccination7b.setVaccinationType(dhppVaccine);
        vaccination7b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination7b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination7b);

        // Animal 8 - Snowball (Rabbit)
        Vaccination vaccination8 = new Vaccination();
        vaccination8.setAnimal(animal8);
        vaccination8.setVeterinarian(veterinarian1);
        vaccination8.setVaccinationType(rhdvVaccine);
        vaccination8.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination8.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination8);

        // Animal 9 - Mittens (Cat)
        Vaccination vaccination9a = new Vaccination();
        vaccination9a.setAnimal(animal9);
        vaccination9a.setVeterinarian(veterinarian1);
        vaccination9a.setVaccinationType(rabiesVaccine);
        vaccination9a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination9a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination9a);

        Vaccination vaccination9b = new Vaccination();
        vaccination9b.setAnimal(animal9);
        vaccination9b.setVeterinarian(veterinarian1);
        vaccination9b.setVaccinationType(fvrcpVaccine);
        vaccination9b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination9b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination9b);

        // Animal 11 - Rocky (Dog)
        Vaccination vaccination11a = new Vaccination();
        vaccination11a.setAnimal(animal11);
        vaccination11a.setVeterinarian(veterinarian1);
        vaccination11a.setVaccinationType(rabiesVaccine);
        vaccination11a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination11a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination11a);

        Vaccination vaccination11b = new Vaccination();
        vaccination11b.setAnimal(animal11);
        vaccination11b.setVeterinarian(veterinarian1);
        vaccination11b.setVaccinationType(dhppVaccine);
        vaccination11b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination11b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination11b);

        // Animal 12 - Daisy (Dog)
        Vaccination vaccination12a = new Vaccination();
        vaccination12a.setAnimal(animal12);
        vaccination12a.setVeterinarian(veterinarian1);
        vaccination12a.setVaccinationType(rabiesVaccine);
        vaccination12a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination12a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination12a);

        Vaccination vaccination12b = new Vaccination();
        vaccination12b.setAnimal(animal12);
        vaccination12b.setVeterinarian(veterinarian1);
        vaccination12b.setVaccinationType(dhppVaccine);
        vaccination12b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination12b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination12b);

        // Animal 13 - Shadow (Cat)
        Vaccination vaccination13a = new Vaccination();
        vaccination13a.setAnimal(animal13);
        vaccination13a.setVeterinarian(veterinarian1);
        vaccination13a.setVaccinationType(rabiesVaccine);
        vaccination13a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination13a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination13a);

        Vaccination vaccination13b = new Vaccination();
        vaccination13b.setAnimal(animal13);
        vaccination13b.setVeterinarian(veterinarian1);
        vaccination13b.setVaccinationType(fvrcpVaccine);
        vaccination13b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination13b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination13b);

        // Animal 14 - Chloe (Cat)
        Vaccination vaccination14a = new Vaccination();
        vaccination14a.setAnimal(animal14);
        vaccination14a.setVeterinarian(veterinarian1);
        vaccination14a.setVaccinationType(rabiesVaccine);
        vaccination14a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination14a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination14a);

        Vaccination vaccination14b = new Vaccination();
        vaccination14b.setAnimal(animal14);
        vaccination14b.setVeterinarian(veterinarian1);
        vaccination14b.setVaccinationType(fvrcpVaccine);
        vaccination14b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination14b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination14b);

        // Animal 15 - Duke (Dog)
        Vaccination vaccination15a = new Vaccination();
        vaccination15a.setAnimal(animal15);
        vaccination15a.setVeterinarian(veterinarian1);
        vaccination15a.setVaccinationType(rabiesVaccine);
        vaccination15a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination15a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination15a);

        Vaccination vaccination15b = new Vaccination();
        vaccination15b.setAnimal(animal15);
        vaccination15b.setVeterinarian(veterinarian1);
        vaccination15b.setVaccinationType(dhppVaccine);
        vaccination15b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination15b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination15b);

        // Animal 16 - Thumper (Rabbit)
        Vaccination vaccination16 = new Vaccination();
        vaccination16.setAnimal(animal16);
        vaccination16.setVeterinarian(veterinarian1);
        vaccination16.setVaccinationType(rhdvVaccine);
        vaccination16.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination16.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination16);

        // Animal 17 - Kiwi (Bird)
        Vaccination vaccination17 = new Vaccination();
        vaccination17.setAnimal(animal17);
        vaccination17.setVeterinarian(veterinarian1);
        vaccination17.setVaccinationType(avianVaccine);
        vaccination17.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination17.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination17);

        // Animal 18 - Molly (Dog)
        Vaccination vaccination18a = new Vaccination();
        vaccination18a.setAnimal(animal18);
        vaccination18a.setVeterinarian(veterinarian1);
        vaccination18a.setVaccinationType(rabiesVaccine);
        vaccination18a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination18a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination18a);

        Vaccination vaccination18b = new Vaccination();
        vaccination18b.setAnimal(animal18);
        vaccination18b.setVeterinarian(veterinarian1);
        vaccination18b.setVaccinationType(dhppVaccine);
        vaccination18b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination18b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination18b);

        // Animal 19 - Oliver (Cat)
        Vaccination vaccination19a = new Vaccination();
        vaccination19a.setAnimal(animal19);
        vaccination19a.setVeterinarian(veterinarian1);
        vaccination19a.setVaccinationType(rabiesVaccine);
        vaccination19a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination19a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination19a);

        Vaccination vaccination19b = new Vaccination();
        vaccination19b.setAnimal(animal19);
        vaccination19b.setVeterinarian(veterinarian1);
        vaccination19b.setVaccinationType(fvrcpVaccine);
        vaccination19b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination19b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination19b);

        // Animal 20 - Sadie (Dog)
        Vaccination vaccination20a = new Vaccination();
        vaccination20a.setAnimal(animal20);
        vaccination20a.setVeterinarian(veterinarian1);
        vaccination20a.setVaccinationType(rabiesVaccine);
        vaccination20a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination20a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination20a);

        Vaccination vaccination20b = new Vaccination();
        vaccination20b.setAnimal(animal20);
        vaccination20b.setVeterinarian(veterinarian1);
        vaccination20b.setVaccinationType(dhppVaccine);
        vaccination20b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination20b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination20b);

        // Animal 21 - Zeus (Dog)
        Vaccination vaccination21a = new Vaccination();
        vaccination21a.setAnimal(animal21);
        vaccination21a.setVeterinarian(veterinarian1);
        vaccination21a.setVaccinationType(rabiesVaccine);
        vaccination21a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination21a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination21a);

        Vaccination vaccination21b = new Vaccination();
        vaccination21b.setAnimal(animal21);
        vaccination21b.setVeterinarian(veterinarian1);
        vaccination21b.setVaccinationType(dhppVaccine);
        vaccination21b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination21b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination21b);

        // Animal 22 - Sophie (Dog)
        Vaccination vaccination22a = new Vaccination();
        vaccination22a.setAnimal(animal22);
        vaccination22a.setVeterinarian(veterinarian1);
        vaccination22a.setVaccinationType(rabiesVaccine);
        vaccination22a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination22a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination22a);

        Vaccination vaccination22b = new Vaccination();
        vaccination22b.setAnimal(animal22);
        vaccination22b.setVeterinarian(veterinarian1);
        vaccination22b.setVaccinationType(dhppVaccine);
        vaccination22b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination22b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination22b);

        // Animal 23 - Teddy (Dog)
        Vaccination vaccination23a = new Vaccination();
        vaccination23a.setAnimal(animal23);
        vaccination23a.setVeterinarian(veterinarian1);
        vaccination23a.setVaccinationType(rabiesVaccine);
        vaccination23a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination23a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination23a);

        Vaccination vaccination23b = new Vaccination();
        vaccination23b.setAnimal(animal23);
        vaccination23b.setVeterinarian(veterinarian1);
        vaccination23b.setVaccinationType(dhppVaccine);
        vaccination23b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination23b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination23b);

        // Animal 30 - Rio (Bird)
        Vaccination vaccination30 = new Vaccination();
        vaccination30.setAnimal(animal30);
        vaccination30.setVeterinarian(veterinarian1);
        vaccination30.setVaccinationType(avianVaccine);
        vaccination30.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination30.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination30);

        // Animal 31 - Sunny (Bird)
        Vaccination vaccination31 = new Vaccination();
        vaccination31.setAnimal(animal31);
        vaccination31.setVeterinarian(veterinarian1);
        vaccination31.setVaccinationType(avianVaccine);
        vaccination31.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination31.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination31);

        // Animal 32 - Clover (Rabbit)
        Vaccination vaccination32 = new Vaccination();
        vaccination32.setAnimal(animal32);
        vaccination32.setVeterinarian(veterinarian1);
        vaccination32.setVaccinationType(rhdvVaccine);
        vaccination32.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination32.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination32);

        // Animal 33 - Cooper (Dog)
        Vaccination vaccination33a = new Vaccination();
        vaccination33a.setAnimal(animal33);
        vaccination33a.setVeterinarian(veterinarian1);
        vaccination33a.setVaccinationType(rabiesVaccine);
        vaccination33a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination33a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination33a);

        Vaccination vaccination33b = new Vaccination();
        vaccination33b.setAnimal(animal33);
        vaccination33b.setVeterinarian(veterinarian1);
        vaccination33b.setVaccinationType(dhppVaccine);
        vaccination33b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination33b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination33b);

        // Animal 34 - Nala (Cat)
        Vaccination vaccination34a = new Vaccination();
        vaccination34a.setAnimal(animal34);
        vaccination34a.setVeterinarian(veterinarian1);
        vaccination34a.setVaccinationType(rabiesVaccine);
        vaccination34a.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination34a.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination34a);

        Vaccination vaccination34b = new Vaccination();
        vaccination34b.setAnimal(animal34);
        vaccination34b.setVeterinarian(veterinarian1);
        vaccination34b.setVaccinationType(fvrcpVaccine);
        vaccination34b.setDateAdministered(dateFormat.parse("2025-12-13"));
        vaccination34b.setNextDueDate(dateFormat.parse("2026-12-13"));
        vaccinationRepository.save(vaccination34b);


        /* Foster Care */
        FosterCare fosterCare1 = new FosterCare();
        fosterCare1.setFosterParent(user1);
        fosterCare1.setAnimal(animal2);
        fosterCare1.setStartDate(dateFormat.parse("2023-08-01"));
        fosterCare1.setEndDate(dateFormat.parse("2023-09-01"));
        fosterCare1.setIsActive(true);
        fosterCareRepository.save(fosterCare1);


        /* Adoption Application */
        AdoptionApplication adoptionApplication1 = new AdoptionApplication();
        adoptionApplication1.setUser(user1);
        adoptionApplication1.setAnimal(animal3);
        adoptionApplication1.setApplicationDate(dateFormat.parse("2023-09-15"));
        adoptionApplication1.setDescription("I have a big garden and lots of love to give.");
        adoptionApplication1.setStatus(Status.PENDING);
        adoptionApplication1.setReviewedByUser(null);
        adoptionApplication1.setIsActive(true);
        adoptionApplicationRepository.save(adoptionApplication1);

        System.out.println("Database seeded successfully with initial data!");
    }
}