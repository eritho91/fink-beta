package se.iths.erikthorell.finkbeta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.erikthorell.finkbeta.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
