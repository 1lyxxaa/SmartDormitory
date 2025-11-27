package com.service.impl;

import com.model.dto.request.CreateRepairRequest;
import com.model.dto.request.UpdateRepairStatus;
import com.model.dto.response.RepairRequestResponse;
import com.model.entity.RepairRequest;
import com.model.entity.User;
import com.model.enums.RepairStatus;
import com.repository.RepairRequestRepository;
import com.repository.UserRepository;
import com.service.RepairService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RepairServiceImpl implements RepairService {

    private final RepairRequestRepository repairRequestRepository;
    private final UserRepository userRepository;

    @Override
    public RepairRequestResponse createRequest(String email, CreateRepairRequest request) {
        // Ищем автора заявки по email из токена
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Собираем новую сущность заявки и сохраняем
        RepairRequest repairRequest = RepairRequest.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .roomNumber(request.getRoomNumber())
                .repairType(request.getRepairType())
                .status(RepairStatus.PENDING)
                .requestedBy(user)
                .build();
        RepairRequest saved = repairRequestRepository.save(repairRequest);
        return RepairRequestResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepairRequestResponse> getRequestsForUser(String email) {
        // Студент видит только свои записи
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return repairRequestRepository.findAllByRequestedBy_Id(user.getId()).stream()
                .map(RepairRequestResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepairRequestResponse> getAllRequests() {
        return repairRequestRepository.findAll().stream()
                .map(RepairRequestResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RepairRequestResponse getById(Long id) {
        RepairRequest request = repairRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Repair request not found"));
        return RepairRequestResponse.fromEntity(request);
    }

    @Override
    public RepairRequestResponse updateStatus(Long id, UpdateRepairStatus request) {
        // Персонал меняет статус на нужный
        RepairRequest entity = repairRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Repair request not found"));
        entity.setStatus(request.getStatus());
        return RepairRequestResponse.fromEntity(repairRequestRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        // Перед удалением убеждаемся, что запись была
        if (!repairRequestRepository.existsById(id)) {
            throw new EntityNotFoundException("Repair request not found");
        }
        repairRequestRepository.deleteById(id);
    }
}

