package com.example.doan.Repository;

import com.example.doan.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from User u where u.email=?1")
    User findByEmail(String email);

    Optional<User> findById(Long id);

    Long countById(Long id);



}
