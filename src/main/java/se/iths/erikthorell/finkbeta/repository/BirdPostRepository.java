package se.iths.erikthorell.finkbeta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import se.iths.erikthorell.finkbeta.model.BirdPost;

@Controller
public interface BirdPostRepository extends JpaRepository<BirdPost, Long> {
}
