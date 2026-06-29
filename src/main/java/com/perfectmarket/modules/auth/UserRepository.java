package com.perfectmarket.modules.auth;

import com.perfectmarket.modules.admin.dto.response.SnapshotUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    Optional<User> findById(UUID id);

    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT u FROM User u")
    Page<SnapshotUserResponse> findUserList(Pageable pageable);
}
