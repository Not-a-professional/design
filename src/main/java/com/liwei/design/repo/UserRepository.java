package com.liwei.design.repo;

import com.liwei.design.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u from User u where u.username = ?1")
    User findAllByUsername(String username);

    @Query("select u from User u where u.email = ?1")
    List<User> findByEmail(String email);
}
