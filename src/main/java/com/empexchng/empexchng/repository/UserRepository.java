package com.empexchng.empexchng.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empexchng.empexchng.model.User;


@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String email);

    List<User> findByRole(String role);

    @Query("SELECT u.role FROM User u WHERE u.email = ?1")
    String getRoleByEmail(String email);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    @Modifying
  @Query("update User u set u.name = :name, u.photoUrl = :photo where u.userId = :id")
  int updateProfile(@Param("id") String userId, @Param("name") String name, @Param("photo") String photoUrl);
}