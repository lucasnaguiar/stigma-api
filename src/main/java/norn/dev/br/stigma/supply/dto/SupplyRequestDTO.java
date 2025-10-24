package norn.dev.br.stigma.supply.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyRequestDTO {

    @NotBlank(message = "Nome do material é obrigatório")
    @Size(max = 255, message = "Nome do material deve ter no máximo 255 caracteres")
    private String supplyName;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String description;

    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Min(value = 0, message = "Quantidade em estoque não pode ser negativa")
    private Integer stockQuantity;

    @NotBlank(message = "Unidade de medida é obrigatória")
    @Size(max = 20, message = "Unidade de medida deve ter no máximo 20 caracteres")
    private String unitOfMeasure;

    @NotNull(message = "Ponto de reposição é obrigatório")
    @Min(value = 0, message = "Ponto de reposição não pode ser negativo")
    private Integer reorderPoint;
}
