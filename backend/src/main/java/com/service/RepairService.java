package com.service;

import com.model.dto.request.CreateRepairRequest;
import com.model.dto.request.UpdateRepairStatus;
import com.model.dto.response.RepairRequestResponse;

import java.util.List;

public interface RepairService {

    RepairRequestResponse createRequest(String email, CreateRepairRequest request);

    List<RepairRequestResponse> getRequestsForUser(String email);

    List<RepairRequestResponse> getAllRequests();

    RepairRequestResponse getById(Long id);

    RepairRequestResponse updateStatus(Long id, UpdateRepairStatus request);

    void delete(Long id);
}

