package norn.dev.br.stigma.supply.repository;

import norn.dev.br.stigma.supply.model.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    List<Supply> findByStudioId(Long studioId);

    Optional<Supply> findByIdAndStudioId(Long id, Long studioId);

    boolean existsBySupplyNameAndStudioId(String supplyName, Long studioId);

    boolean existsBySupplyNameAndStudioIdAndIdNot(String supplyName, Long studioId, Long id);

    List<Supply> findByStudioIdAndStockQuantityLessThanEqualReorderPoint(Long studioId);
}
