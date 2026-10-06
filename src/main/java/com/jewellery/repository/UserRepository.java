package com.jewellery.repository;

import com.jewellery.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

// You MUST have this import for the Controller to work without errors
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // Notice the Optional<User> here!
    Optional<User> findByEmail(String email);
}