package se.iths.erikthorell.finkbeta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.iths.erikthorell.finkbeta.model.Friendship;
import se.iths.erikthorell.finkbeta.model.User;
import se.iths.erikthorell.finkbeta.repository.FriendshipRepository;
import se.iths.erikthorell.finkbeta.repository.UserRepository;

import java.security.Principal;

@Controller
@RequestMapping("/friends")
public class FriendshipController {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipController(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/request/{receiverId}")
    public String sendFriendRequest(@PathVariable Long receiverId, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }

        User sender = userRepository.findByUsername(principal.getName()).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();

        if (sender.getId().equals(receiver.getId())) {
            return "redirect:/home";
        }

        if (friendshipRepository.findBySenderAndReceiver(sender, receiver).isPresent()
                || friendshipRepository.findBySenderAndReceiver(receiver, sender).isPresent()) {
            return "redirect:/home";
        }

        Friendship friendship = new Friendship();
        friendship.setSender(sender);
        friendship.setReceiver(receiver);
        friendship.setStatus("PENDING");

        friendshipRepository.save(friendship);

        return "redirect:/home";
    }

    @PostMapping("/accept/{id}")
    public String acceptFriendRequest(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }

        User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        Friendship friendship = friendshipRepository.findById(id).orElseThrow();

        if (!friendship.getReceiver().getId().equals(currentUser.getId())) {
            return "redirect:/home";
        }

        friendship.setStatus("ACCEPTED");
        friendshipRepository.save(friendship);

        return "redirect:/home";
    }
}