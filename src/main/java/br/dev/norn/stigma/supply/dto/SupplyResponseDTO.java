package br.dev.norn.stigma.supply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.dev.norn.stigma.supply.model.Supply;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyResponseDTO {

    private Long id;
    private Long studioId;
    private String supplyName;
    private String description;
    private Integer stockQuantity;
    private String unitOfMeasure;
    private Integer reorderPoint;
    private Boolean needsReorder;

    public static SupplyResponseDTO fromEntity(Supply supply) {
        return SupplyResponseDTO.builder()
                .id(supply.getId())
                .studioId(supply.getStudioId())
                .supplyName(supply.getSupplyName())
                .description(supply.getDescription())
                .stockQuantity(supply.getStockQuantity())
                .unitOfMeasure(supply.getUnitOfMeasure())
                .reorderPoint(supply.getReorderPoint())
                .needsReorder(supply.getStockQuantity() <= supply.getReorderPoint())
                .build();
    }
}
