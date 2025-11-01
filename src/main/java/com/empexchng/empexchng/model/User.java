package com.empexchng.empexchng.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @Column(name = "user_id")
  private String userId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "email", nullable = false, length = 150, unique = true)
  private String email;

  // store the password (ideally a hash) as VARCHAR(255)
  @Column(name = "password", nullable = false, length = 255)
  private String password;

  @Column(name = "photo_url", length = 500)
  private String photoUrl;

  @Column(name = "role", nullable = false, length = 50)
  private String role;
}
