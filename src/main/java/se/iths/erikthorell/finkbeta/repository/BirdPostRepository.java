package se.iths.erikthorell.finkbeta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.iths.erikthorell.finkbeta.model.BirdPost;

@Repository
public interface BirdPostRepository extends JpaRepository<BirdPost, Long> {
}
