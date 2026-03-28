package se.iths.erikthorell.finkbeta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.iths.erikthorell.finkbeta.model.Friendship;
import se.iths.erikthorell.finkbeta.model.User;
import se.iths.erikthorell.finkbeta.repository.FriendshipRepository;
import se.iths.erikthorell.finkbeta.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public HomeController(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal == null) {
            return "index";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        List<Friendship> incomingRequests =
                friendshipRepository.findByReceiverAndStatus(user, "PENDING");

        List<Friendship> friends =
                friendshipRepository.findBySenderOrReceiverAndStatus(user, user, "ACCEPTED");

        List<User> allUsers = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("incomingRequests", incomingRequests);
        model.addAttribute("friends", friends);
        model.addAttribute("allUsers", allUsers);

        return "home";
    }
}