package com.example.customsale.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;
    private String pwd_hash;

    private LocalDateTime createdAt;
    private String email;
    private String phoneNumber;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="user")
    private List<Product> products;

    public static User create(String name, String pwd_hash, String email, String phoneNumber) {
        return new User(null, name, pwd_hash, LocalDateTime.now(), email, phoneNumber, Collections.emptyList());
    }
}
