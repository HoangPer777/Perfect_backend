package com.perfectmarket.modules.auth;

import com.perfectmarket.modules.auth.dto.SnapshotUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    Optional<User> findById(UUID id);

    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r " +
            "WHERE (:role IS NULL OR r.name = :role) " +
            "AND (:username IS NULL OR u.username ILIKE CONCAT('%', :username, '%'))")
    Page<SnapshotUserResponse> findUserList(@Param("role") String role,
                                            @Param("username") String username,
                                            Pageable pageable);
}
