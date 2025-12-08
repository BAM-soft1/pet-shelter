package org.pet.backendpetshelter.Mongo.Service;

import org.pet.backendpetshelter.Mongo.Entity.UserDocument;
import org.pet.backendpetshelter.Mongo.Repository.UserMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("mongo")
public class UserMongoService {

    private final UserMongoRepository userRepository;

    public UserMongoService(UserMongoRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDocument> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDocument getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserDocument addUser(UserDocument user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        return userRepository.save(user);
    }

    public UserDocument updateUser(String id, UserDocument request) {
        UserDocument user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setIsActive(request.getIsActive());
        user.setRole(request.getRole());
        
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
