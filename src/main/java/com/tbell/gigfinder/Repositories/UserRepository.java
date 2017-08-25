package com.tbell.gigfinder.Repositories;

import com.tbell.gigfinder.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
