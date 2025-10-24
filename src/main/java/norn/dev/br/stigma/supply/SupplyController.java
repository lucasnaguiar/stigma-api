package norn.dev.br.stigma.supply;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import norn.dev.br.stigma.supply.dto.SupplyRequestDTO;
import norn.dev.br.stigma.supply.dto.SupplyResponseDTO;
import norn.dev.br.stigma.supply.service.SupplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@RequiredArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;

    @GetMapping
    public ResponseEntity<List<SupplyResponseDTO>> findAll() {
        List<SupplyResponseDTO> supplies = supplyService.findAll();
        return ResponseEntity.ok(supplies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyResponseDTO> findById(@PathVariable Long id) {
        SupplyResponseDTO supply = supplyService.findById(id);
        return ResponseEntity.ok(supply);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<SupplyResponseDTO>> findLowStock() {
        List<SupplyResponseDTO> supplies = supplyService.findLowStock();
        return ResponseEntity.ok(supplies);
    }

    @PostMapping
    public ResponseEntity<SupplyResponseDTO> create(@Valid @RequestBody SupplyRequestDTO requestDTO) {
        SupplyResponseDTO createdSupply = supplyService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupply);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplyRequestDTO requestDTO) {
        SupplyResponseDTO updatedSupply = supplyService.update(id, requestDTO);
        return ResponseEntity.ok(updatedSupply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
