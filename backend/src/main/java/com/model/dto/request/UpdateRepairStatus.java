package com.model.dto.request;

import com.model.enums.RepairStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRepairStatus {

    @NotNull
    private RepairStatus status;
}

