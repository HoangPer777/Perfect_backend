package com.perfectmarket.modules.auth;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    private String passwordHash;

    private String fullName;

    @Column(unique = true)
    private String username;

    private String avatarUrl;

    // --- Cấu trúc đầy đủ của Hoàng ---
    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE | INACTIVE | BANNED

    @Column(nullable = false)
    @Builder.Default
    private boolean isVerified = false;

    /** LOCAL | GOOGLE | FACEBOOK */
    @Column(nullable = false)
    @Builder.Default
    private String provider = "LOCAL";

    private String providerId;

    private String city;

    private String detailedAddress;

    @Column(nullable = false)
    @Builder.Default
    private boolean emailNotifications = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean promotionalOffers = false;

    private String specialization;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String portfolioUrl;

    private String skills;

    @Column(nullable = false)
    @Builder.Default
    private Integer experienceYears = 0;
    // ---------------------------------

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}