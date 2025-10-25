package br.dev.norn.stigma.user.repository;

import br.dev.norn.stigma.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByStudioId(Long studioId);

    Optional<User> findByIdAndStudioId(Long id, Long studioId);

    Optional<User> findByEmailAndStudioId(String email, Long studioId);

    boolean existsByEmailAndStudioId(String email, Long studioId);

    boolean existsByEmailAndStudioIdAndIdNot(String email, Long studioId, Long id);
}
