package se.iths.erikthorell.finkbeta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.erikthorell.finkbeta.model.Friendship;
import se.iths.erikthorell.finkbeta.model.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findByReceiverAndStatus(User receiver, String status);

    List<Friendship> findBySenderAndStatus(User sender, String status);

    List<Friendship> findBySenderOrReceiverAndStatus(User sender, User receiver, String status);

    Optional<Friendship> findBySenderAndReceiver(User sender, User receiver);
}