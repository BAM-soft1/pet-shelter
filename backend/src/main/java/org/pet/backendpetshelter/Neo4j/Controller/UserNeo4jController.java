package org.pet.backendpetshelter.Neo4j.Controller;

import org.pet.backendpetshelter.Neo4j.Entity.UserNode;
import org.pet.backendpetshelter.Neo4j.Service.UserNeo4jService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neo4j/users")
@CrossOrigin
@Profile("neo4j")
public class UserNeo4jController {

    private final UserNeo4jService userService;

    public UserNeo4jController(UserNeo4jService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserNode> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserNode getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<UserNode> addUser(@RequestBody UserNode user) {
        return ResponseEntity.status(201).body(userService.addUser(user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserNode> updateUser(@PathVariable String id, @RequestBody UserNode user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
