package se.iths.erikthorell.finkbeta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.erikthorell.finkbeta.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
