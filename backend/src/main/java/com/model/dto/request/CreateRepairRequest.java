package com.model.dto.request;

import com.model.enums.RepairType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRepairRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String roomNumber;

    @NotNull
    private RepairType repairType;
}

