package com.example.teamboolean.apprentidash;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);

}
