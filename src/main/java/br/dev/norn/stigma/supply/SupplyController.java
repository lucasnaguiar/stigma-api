package br.dev.norn.stigma.supply;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import br.dev.norn.stigma.supply.dto.SupplyRequestDTO;
import br.dev.norn.stigma.supply.dto.SupplyResponseDTO;
import br.dev.norn.stigma.supply.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@RequiredArgsConstructor
public class SupplyController {
    @Autowired
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
