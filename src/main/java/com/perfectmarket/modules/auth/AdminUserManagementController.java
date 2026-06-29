package com.perfectmarket.modules.auth;

import com.perfectmarket.modules.auth.dto.SnapshotUserResponse;
import com.perfectmarket.modules.auth.dto.UpdateUserRoleRequest;
import com.perfectmarket.modules.auth.dto.UpdateUserStatusRequest;
import com.perfectmarket.modules.auth.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users/")
@RequiredArgsConstructor
public class AdminUserManagementController {
    private final AdminUserManagementService adminUserManagementService;

    @GetMapping("/get-user-list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Page<SnapshotUserResponse>> getUserList(@RequestParam(required = false) String role,
                                                                  @RequestParam(required = false, defaultValue = "") String username, Pageable pageable) {
        return ResponseEntity.ok(adminUserManagementService.getUserList(role, username, pageable));
    }

    @GetMapping("/user-info")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserInfoResponse> userInfo(@RequestParam UUID id) {
        return ResponseEntity.ok(adminUserManagementService.getUserInfo(id));
    }

    @GetMapping("/get-roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserInfoResponse.RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(adminUserManagementService.getAllRoles());
    }

    @PutMapping("/update-role")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> updateUserRole(@RequestBody UpdateUserRoleRequest request) {
        return ResponseEntity.ok(adminUserManagementService.updateUserRole(request));
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> updateUserStatus(@RequestBody UpdateUserStatusRequest request) {
        return ResponseEntity.ok(adminUserManagementService.updateUserStatus(request));
    }

}
