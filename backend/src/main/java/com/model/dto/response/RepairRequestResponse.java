package com.model.dto.response;

import com.model.entity.RepairRequest;
import com.model.enums.RepairStatus;
import com.model.enums.RepairType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class RepairRequestResponse {
    Long id;
    String title;
    String description;
    String roomNumber;
    RepairType repairType;
    RepairStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long requestedById;
    String requestedByName;

    public static RepairRequestResponse fromEntity(RepairRequest entity) {
        return RepairRequestResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .roomNumber(entity.getRoomNumber())
                .repairType(entity.getRepairType())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .requestedById(entity.getRequestedBy() != null ? entity.getRequestedBy().getId() : null)
                .requestedByName(entity.getRequestedBy() != null ? entity.getRequestedBy().getFullName() : null)
                .build();
    }
}

