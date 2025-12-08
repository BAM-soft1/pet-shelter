package org.pet.backendpetshelter.Neo4j.Service;

import org.pet.backendpetshelter.Neo4j.Entity.UserNode;
import org.pet.backendpetshelter.Neo4j.Repository.UserNeo4jRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("neo4j")
public class UserNeo4jService {

    private final UserNeo4jRepository userRepository;

    public UserNeo4jService(UserNeo4jRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserNode> getAllUsers() {
        return userRepository.findAll();
    }

    public UserNode getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserNode addUser(UserNode user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        return userRepository.save(user);
    }

    public UserNode updateUser(String id, UserNode request) {
        UserNode user = userRepository.findById(id)
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
