package norn.dev.br.stigma.supply.service;

import lombok.RequiredArgsConstructor;
import norn.dev.br.stigma.supply.dto.SupplyRequestDTO;
import norn.dev.br.stigma.supply.dto.SupplyResponseDTO;
import norn.dev.br.stigma.supply.model.Supply;
import norn.dev.br.stigma.supply.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplyService {

    private static final Long STUDIO_ID = 1L;
    @Autowired
    private SupplyRepository supplyRepository;

    @Transactional(readOnly = true)
    public List<SupplyResponseDTO> findAll() {
        return supplyRepository.findByStudioId(STUDIO_ID)
                .stream()
                .map(SupplyResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplyResponseDTO findById(Long id) {
        Supply supply = supplyRepository.findByIdAndStudioId(id, STUDIO_ID)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com id: " + id));
        return SupplyResponseDTO.fromEntity(supply);
    }

    @Transactional
    public SupplyResponseDTO create(SupplyRequestDTO requestDTO) {
        if (supplyRepository.existsBySupplyNameAndStudioId(requestDTO.getSupplyName(), STUDIO_ID)) {
            throw new RuntimeException("Já existe um material com o nome: " + requestDTO.getSupplyName());
        }

        Supply supply = Supply.builder()
                .studioId(STUDIO_ID)
                .supplyName(requestDTO.getSupplyName())
                .description(requestDTO.getDescription())
                .stockQuantity(requestDTO.getStockQuantity())
                .unitOfMeasure(requestDTO.getUnitOfMeasure())
                .reorderPoint(requestDTO.getReorderPoint())
                .build();

        Supply savedSupply = supplyRepository.save(supply);
        return SupplyResponseDTO.fromEntity(savedSupply);
    }

    @Transactional
    public SupplyResponseDTO update(Long id, SupplyRequestDTO requestDTO) {
        Supply supply = supplyRepository.findByIdAndStudioId(id, STUDIO_ID)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com id: " + id));

        if (supplyRepository.existsBySupplyNameAndStudioIdAndIdNot(requestDTO.getSupplyName(), STUDIO_ID, id)) {
            throw new RuntimeException("Já existe outro material com o nome: " + requestDTO.getSupplyName());
        }

        supply.setSupplyName(requestDTO.getSupplyName());
        supply.setDescription(requestDTO.getDescription());
        supply.setStockQuantity(requestDTO.getStockQuantity());
        supply.setUnitOfMeasure(requestDTO.getUnitOfMeasure());
        supply.setReorderPoint(requestDTO.getReorderPoint());

        Supply updatedSupply = supplyRepository.save(supply);
        return SupplyResponseDTO.fromEntity(updatedSupply);
    }

    @Transactional
    public void delete(Long id) {
        Supply supply = supplyRepository.findByIdAndStudioId(id, STUDIO_ID)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com id: " + id));
        supplyRepository.delete(supply);
    }
}
