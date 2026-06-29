package com.perfectmarket.modules.admin;

import com.perfectmarket.modules.admin.dto.response.SnapshotUserResponse;
import com.perfectmarket.modules.admin.dto.response.UserInfoResponse;
import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Page<SnapshotUserResponse> getUserList(Pageable pageable) {
        return userRepository.findUserList(pageable);
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
}
