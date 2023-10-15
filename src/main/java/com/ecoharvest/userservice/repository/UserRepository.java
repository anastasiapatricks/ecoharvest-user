package com.ecoharvest.userservice.repository;

import com.ecoharvest.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    User findById(Long id);

    @Query("select u from User u")
    List<User> getUserList();

    @Modifying
    @Query("UPDATE User u " +
            "SET u.name = CASE WHEN :name IS NOT NULL THEN :name ELSE u.name END, " +
            "u.email = CASE WHEN :email IS NOT NULL THEN :email ELSE u.email END, " +
            "u.contactNo = CASE WHEN :contactNo IS NOT NULL THEN :contactNo ELSE u.contactNo END " +
            "WHERE u.id = :id")
    void updateUserDetails(@Param("id") Long id, @Param("name") String name, @Param("email") String email, @Param("contactNo") String contactNo);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    void changePassword(@Param("id") Long id, @Param("password") String password);
}
