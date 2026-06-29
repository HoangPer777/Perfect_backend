package com.perfectmarket.modules.auth;

import com.perfectmarket.modules.auth.dto.SnapshotUserResponse;
import com.perfectmarket.modules.auth.dto.UpdateUserRoleRequest;
import com.perfectmarket.modules.auth.dto.UpdateUserStatusRequest;
import com.perfectmarket.modules.auth.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserManagementService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public Page<SnapshotUserResponse> getUserList(String role, String username, Pageable pageable) {
        return userRepository.findUserList(role, username, pageable);
    }

    public UserInfoResponse getUserInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Set<UserInfoResponse.RoleResponse> roleResponses = user.getRoles().stream()
                .map(role -> UserInfoResponse.RoleResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toSet());

        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .roles(roleResponses)
                .city(user.getCity())
                .detailedAddress(user.getDetailedAddress())
                .specialization(user.getSpecialization())
                .bio(user.getBio())
                .portfolioUrl(user.getPortfolioUrl())
                .skills(user.getSkills())
                .experienceYears(user.getExperienceYears())
                .status(user.getStatus())
                .isVerified(user.isVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public List<UserInfoResponse.RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(r -> new UserInfoResponse.RoleResponse(r.getId(), r.getName())).collect(Collectors.toList());
    }

    @Transactional
    public boolean updateUserRole(UpdateUserRoleRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + request.userId()));

        Set<Role> roles = user.getRoles();

        Role targetRole = roleRepository.getReferenceById(request.roleId());

        if (roles.contains(targetRole)) {
            if (roles.size() == 1) {
                throw new IllegalArgumentException("User must retain at least one role!");
            }
            roles.remove(targetRole);
        } else {
            roles.add(targetRole);
        }
        return true;
    }

    @Transactional
    public boolean updateUserStatus(UpdateUserStatusRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + request.userId()));

        user.setStatus(request.status());
        return true;
    }
}
