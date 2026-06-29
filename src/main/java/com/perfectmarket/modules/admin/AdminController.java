package com.perfectmarket.modules.admin;

import com.perfectmarket.modules.admin.dto.response.SnapshotUserResponse;
import com.perfectmarket.modules.admin.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/get-user-list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Page<SnapshotUserResponse>> getUserList(Pageable pageable) {
        return ResponseEntity.ok(adminService.getUserList(pageable));
    }

    @GetMapping("/user-info")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserInfoResponse> userInfo(@RequestParam UUID id) {
        return ResponseEntity.ok(adminService.getUserInfo(id));
    }
}
