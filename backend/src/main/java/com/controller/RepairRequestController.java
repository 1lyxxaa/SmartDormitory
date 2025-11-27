package com.controller;

import com.model.dto.request.CreateRepairRequest;
import com.model.dto.request.UpdateRepairStatus;
import com.model.dto.response.RepairRequestResponse;
import com.service.RepairService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/repairs")
@RequiredArgsConstructor
public class RepairRequestController {

    private final RepairService repairService;

    @PostMapping
    // Создаём новую заявку от текущего пользователя
    public ResponseEntity<RepairRequestResponse> createRequest(@AuthenticationPrincipal UserDetails userDetails,
                                                               @Valid @RequestBody CreateRepairRequest request) {
        RepairRequestResponse response = repairService.createRequest(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    // Студент видит свои заявки, персонал — все
    public ResponseEntity<List<RepairRequestResponse>> getRequests(@AuthenticationPrincipal UserDetails userDetails) {
        if (hasStaffPrivileges(userDetails)) {
            return ResponseEntity.ok(repairService.getAllRequests());
        }
        return ResponseEntity.ok(repairService.getRequestsForUser(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    // Достаём конкретную заявку
    public ResponseEntity<RepairRequestResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(repairService.getById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<RepairRequestResponse> updateStatus(@PathVariable Long id,
                                                              @Valid @RequestBody UpdateRepairStatus request) {
        return ResponseEntity.ok(repairService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repairService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private boolean hasStaffPrivileges(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                        || authority.getAuthority().equals("ROLE_STAFF"));
    }
}

