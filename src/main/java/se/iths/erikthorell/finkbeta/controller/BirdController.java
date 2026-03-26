package se.iths.erikthorell.finkbeta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.iths.erikthorell.finkbeta.model.BirdPost;
import se.iths.erikthorell.finkbeta.model.User;
import se.iths.erikthorell.finkbeta.repository.BirdPostRepository;
import se.iths.erikthorell.finkbeta.repository.UserRepository;

import java.security.Principal;

@Controller
public class BirdController {

    private final BirdPostRepository birdPostRepository;
    private final UserRepository userRepository;

    public BirdController(BirdPostRepository birdPostRepository, UserRepository userRepository) {
        this.birdPostRepository = birdPostRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/birds/new")
    public String newBirdForm(Model model) {
        model.addAttribute("bird", new BirdPost()); // <-- måste finnas för th:object
        return "newBirdPost";
    }

    @PostMapping("/birds")
    public String addBird(@ModelAttribute BirdPost birdPost, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        birdPost.setUser(user);            // sätt användaren som ägare
        birdPostRepository.save(birdPost);
        return "redirect:/home/" + user.getId();
    }
}