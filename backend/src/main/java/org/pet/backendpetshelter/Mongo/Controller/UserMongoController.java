package org.pet.backendpetshelter.Mongo.Controller;

import org.pet.backendpetshelter.Mongo.Entity.UserDocument;
import org.pet.backendpetshelter.Mongo.Service.UserMongoService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo/users")
@CrossOrigin
@Profile("mongo")
public class UserMongoController {

    private final UserMongoService userService;

    public UserMongoController(UserMongoService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDocument> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDocument getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDocument> addUser(@RequestBody UserDocument user) {
        return ResponseEntity.status(201).body(userService.addUser(user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDocument> updateUser(@PathVariable String id, @RequestBody UserDocument user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
